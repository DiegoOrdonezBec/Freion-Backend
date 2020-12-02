package mx.edu.uttt.Freion.controller;

import mx.edu.uttt.Freion.dto.PostRequest;
import mx.edu.uttt.Freion.dto.PostResponse;
import mx.edu.uttt.Freion.model.Post;
import mx.edu.uttt.Freion.model.User;
import mx.edu.uttt.Freion.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/post")
@CrossOrigin
public class PostController {
    @Autowired
    private PostService postService;

    @Autowired
    private AuthService authService;

    @Autowired
    private ContentTypeService contentTypeService;

    @Autowired
    private PrivacyService privacyService;

    @Autowired
    private UserService userService;

    @PostMapping
    public ResponseEntity<Void> createPost(@RequestBody PostRequest postRequest){
        try{
            Post post = Post.builder()
                    .user(authService.getCurrentUser())
                    .date(Instant.now())
                    .contentType(contentTypeService.getContentTypeByValue(postRequest.getContentType()))
                    .content(postRequest.getContent())
                    .privacy(privacyService.getPrivacyByValue(postRequest.getPrivacy()))
                    .build();

            postService.save(post);

            return new ResponseEntity<>(HttpStatus.CREATED);
        }catch(Exception e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping
    public ResponseEntity<List<PostResponse>> getAllPost(){
        List<Post> posts = postService.findAll();
        User currentUser = authService.getCurrentUser();
        List<PostResponse> postsResponses = postService.canView(posts, currentUser);
        return new ResponseEntity<>(postsResponses, HttpStatus.OK);
    }

    @GetMapping("/follow")
    public ResponseEntity<List<PostResponse>> getFollowPosts(){
        List<Post> posts = postService.findAll();
        User currentUser = authService.getCurrentUser();
        posts = posts.stream().filter(post -> {return currentUser.getFollows().contains(post.getUser());}).collect(Collectors.toList());
        List<PostResponse> postsResponses = postService.canView(posts, currentUser);
        return new ResponseEntity<>(postsResponses, HttpStatus.OK);
    }

    @GetMapping("/{username}")
    public ResponseEntity<List<PostResponse>> getAllPostByUsername(@PathVariable String username){
        User user = userService.getByUsername(username);
        List<Post> posts = postService.findByUser(user);
        User currentUser = authService.getCurrentUser();
        List<PostResponse> postsResponses = postService.canView(posts, currentUser);
        return new ResponseEntity<>(postsResponses, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePostById(@PathVariable Long id){
        Post post = postService.findById(id);
        if(post != null && post.getUser().equals(authService.getCurrentUser())){
            postService.deleteById(post.getId());
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }



}
