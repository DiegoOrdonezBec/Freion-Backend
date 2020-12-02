package mx.edu.uttt.Freion.controller;

import mx.edu.uttt.Freion.dto.JWTDTO;
import mx.edu.uttt.Freion.dto.LoginRequest;
import mx.edu.uttt.Freion.dto.MessageResponse;
import mx.edu.uttt.Freion.dto.SignupRequest;
import mx.edu.uttt.Freion.model.User;
import mx.edu.uttt.Freion.service.JWTService;
import mx.edu.uttt.Freion.service.PhotoService;
import mx.edu.uttt.Freion.service.PrivacyService;
import mx.edu.uttt.Freion.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin
public class AuthController {
    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserService userService;

    @Autowired
    PrivacyService privacyService;

    @Autowired
    PhotoService photoService;

    @Autowired
    JWTService jwtService;

    @Autowired
    PasswordEncoder passwordEncoder;

    @PostMapping("/signup")
    public ResponseEntity<MessageResponse> signup(@RequestBody SignupRequest signupRequest){
        try{
            String message ="CREATED SUCCESSFULLY";
            Boolean valid = true;

            if(userService.existsByUsername(signupRequest.getUsername())){
                message = "DUPLICATED USERNAME";
            }
            if(userService.existsByEmail(signupRequest.getEmail())){
                message = "DUPLICATED EMAIL";
            }
            if(signupRequest.getUsername().isEmpty()){
                message = "INVALID USERNAME";
            }
            if(signupRequest.getName().isEmpty()){
                message = "INVALID NAME";
            }

            if(message.equals("CREATED SUCCESSFULLY")){

                User user = User.builder()
                        .name(signupRequest.getName())
                        .username(signupRequest.getUsername())
                        .email(signupRequest.getEmail())
                        .password(passwordEncoder.encode(signupRequest.getPassword()))
                        .profilePhotoUrl("https://material.angular.io/assets/img/examples/shiba1.jpg")
                        .build();

                userService.save(user);

                return new ResponseEntity<>(MessageResponse.builder().message(message).build(), HttpStatus.CREATED);
            }else{
                return new ResponseEntity<>(MessageResponse.builder().message(message).build(), HttpStatus.OK);
            }
        }catch (Exception e){
            return new ResponseEntity<>(MessageResponse.builder().message("SERVER PROBLEMS").build(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<JWTDTO> login(@RequestBody LoginRequest loginRequest){
        try{
            Authentication authentication =
                    authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(),loginRequest.getPassword()));
            SecurityContextHolder.getContext().setAuthentication(authentication);
            String jwt = jwtService.generateToken(authentication);
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            JWTDTO jwtDto = JWTDTO.builder().token(jwt).username(userDetails.getUsername()).build();

            return new ResponseEntity<>(jwtDto, HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/validate")
    public ResponseEntity<Boolean> validate(@RequestBody JWTDTO jwtdto){
        try{
            if(jwtService.validateToken(jwtdto.getToken())){
                return new ResponseEntity<>(true, HttpStatus.OK);
            }else{
                return new ResponseEntity<>(false, HttpStatus.OK);
            }
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(){
        try{
            SecurityContextHolder.getContext().setAuthentication(null);
            return new ResponseEntity<>(HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

}
