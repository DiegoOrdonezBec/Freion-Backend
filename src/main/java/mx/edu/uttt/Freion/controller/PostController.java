package mx.edu.uttt.Freion.controller;

import mx.edu.uttt.Freion.dto.PostRequest;
import mx.edu.uttt.Freion.dto.PostResponse;
import mx.edu.uttt.Freion.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/post")
@CrossOrigin
public class PostController {
    @Autowired
    private PostService postService;

    @PostMapping
    public ResponseEntity<Void> createPost(@RequestBody PostRequest postRequest){
        return postService.createPost(postRequest);
    }

    @GetMapping
    public ResponseEntity<List<PostResponse>> getAllPost(){
        return postService.getAllPost();
    }

    @GetMapping("/follow")
    public ResponseEntity<List<PostResponse>> getFollowPosts(){
        return postService.getFollowPosts();
    }

    @GetMapping("/{username}")
    public ResponseEntity<List<PostResponse>> getAllPostByUsername(@PathVariable String username){
        return postService.getAllPostByUsername(username);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePostById(@PathVariable Long id){
        return postService.deletePostById(id);
    }
}
