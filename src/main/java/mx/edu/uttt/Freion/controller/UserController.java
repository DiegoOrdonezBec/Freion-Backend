package mx.edu.uttt.Freion.controller;

import mx.edu.uttt.Freion.dto.UserRequest;
import mx.edu.uttt.Freion.dto.UserResponse;
import mx.edu.uttt.Freion.model.User;
import mx.edu.uttt.Freion.service.AuthService;
import mx.edu.uttt.Freion.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/user")
@CrossOrigin
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private AuthService authService;

    @GetMapping
    public ResponseEntity<List<UserResponse>> getAllUsers(){
        List<User> users = userService.findAll();
        User currentUser = authService.getCurrentUser();
        List<UserResponse> userResponses = userService.canViewUsers(users, currentUser);
        return new ResponseEntity<>(userResponses, HttpStatus.OK);
    }

    @PostMapping("/block")
    public ResponseEntity<Void> blockByUsername(@RequestBody UserRequest userRequest){
        User blocked = userService.getByUsername(userRequest.getUsername());
        User user = authService.getCurrentUser();
        if(!user.getBlocks().contains(blocked)){
            user.getBlocks().add(blocked);
        }

        userService.save(user);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/block")
    public ResponseEntity<List<UserResponse>> getBlocks(){
        User blocker = authService.getCurrentUser();
        List<UserResponse> blockResponses = blocker.getBlocks().stream()
                .map(user -> {
                    return UserResponse.builder()
                            .id(user.getId())
                            .username(user.getUsername())
                            .build();
                }).collect(Collectors.toList());

        return new ResponseEntity<>(blockResponses, HttpStatus.OK);
    }
}
