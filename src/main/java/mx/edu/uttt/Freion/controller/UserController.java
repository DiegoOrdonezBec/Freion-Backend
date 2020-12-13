package mx.edu.uttt.Freion.controller;

import mx.edu.uttt.Freion.dto.UserRequest;
import mx.edu.uttt.Freion.dto.UserResponse;
import mx.edu.uttt.Freion.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user")
@CrossOrigin
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping
    public ResponseEntity<List<UserResponse>> getAllUsers(){
        return userService.getAllUsers();
    }

    @PostMapping("/block")
    public ResponseEntity<Void> blockByUsername(@RequestBody UserRequest userRequest){
        return userService.blockByUsername(userRequest);
    }

    @GetMapping("/block")
    public ResponseEntity<List<UserResponse>> getBlocks(){
        return userService.getBlocks();
    }

    @GetMapping("/follower")
    public ResponseEntity<List<UserResponse>> getFollowers(){
        return userService.getFollowers();
    }
}
