package mx.edu.uttt.Freion.service;

import mx.edu.uttt.Freion.dto.JWTDTO;
import mx.edu.uttt.Freion.dto.LoginRequest;
import mx.edu.uttt.Freion.dto.MessageResponse;
import mx.edu.uttt.Freion.dto.SignupRequest;
import mx.edu.uttt.Freion.model.User;
import mx.edu.uttt.Freion.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

@Service
@Transactional
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    PrivacyService privacyRepository;

    @Autowired
    PhotoService photoRepository;

    @Autowired
    JWTService jwtService;

    @Autowired
    PasswordEncoder passwordEncoder;

    public ResponseEntity<MessageResponse> signup(SignupRequest signupRequest){
        try{
            String message ="CREATED SUCCESSFULLY";
            Boolean valid = true;

            if(userRepository.existsByUsername(signupRequest.getUsername())){
                message = "DUPLICATED USERNAME";
            }
            if(userRepository.existsByEmail(signupRequest.getEmail())){
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

                userRepository.save(user);

                return new ResponseEntity<>(MessageResponse.builder().message(message).build(), HttpStatus.CREATED);
            }else{
                return new ResponseEntity<>(MessageResponse.builder().message(message).build(), HttpStatus.OK);
            }
        }catch (Exception e){
            return new ResponseEntity<>(MessageResponse.builder().message("SERVER PROBLEMS").build(), HttpStatus.BAD_REQUEST);
        }
    }

    public ResponseEntity<JWTDTO> login(LoginRequest loginRequest){
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

    public ResponseEntity<Boolean> validate(JWTDTO jwtdto){
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

    public ResponseEntity<Void> logout(){
        try{
            SecurityContextHolder.getContext().setAuthentication(null);
            return new ResponseEntity<>(HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    public User getCurrentUser() {
        org.springframework.security.core.userdetails.User principal = (org.springframework.security.core.userdetails.User) SecurityContextHolder.
                getContext().getAuthentication().getPrincipal();

        return userRepository.findByUsername(principal.getUsername()).get();
    }

    public boolean isLoggedIn() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return !(authentication instanceof AnonymousAuthenticationToken) && authentication.isAuthenticated();
    }
}
