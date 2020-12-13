package mx.edu.uttt.Freion.controller;

import mx.edu.uttt.Freion.dto.JWTDTO;
import mx.edu.uttt.Freion.dto.LoginRequest;
import mx.edu.uttt.Freion.dto.MessageResponse;
import mx.edu.uttt.Freion.dto.SignupRequest;
import mx.edu.uttt.Freion.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin
public class AuthController {
    @Autowired
    AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<MessageResponse> signup(@RequestBody SignupRequest signupRequest){
        return authService.signup(signupRequest);
    }

    @PostMapping("/login")
    public ResponseEntity<JWTDTO> login(@RequestBody LoginRequest loginRequest){
        return authService.login(loginRequest);
    }

    @PostMapping("/validate")
    public ResponseEntity<Boolean> validate(@RequestBody JWTDTO jwtdto){
        return authService.validate(jwtdto);
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(){
        return authService.logout();
    }

}
