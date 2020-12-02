package mx.edu.uttt.Freion.controller;

import mx.edu.uttt.Freion.dto.CommentRequest;
import mx.edu.uttt.Freion.dto.CommentResponse;
import mx.edu.uttt.Freion.model.Comment;
import mx.edu.uttt.Freion.model.Post;
import mx.edu.uttt.Freion.model.User;
import mx.edu.uttt.Freion.service.AuthService;
import mx.edu.uttt.Freion.service.CommentService;
import mx.edu.uttt.Freion.service.ContentTypeService;
import mx.edu.uttt.Freion.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;

@RestController
@RequestMapping("/api/comment")
@CrossOrigin
public class CommentController {
    @Autowired
    private CommentService commentService;

    @Autowired
    private PostService postService;

    @Autowired
    private AuthService authService;

    @Autowired
    private ContentTypeService contentTypeService;

    @PostMapping
    public ResponseEntity<Void> createComment(@RequestBody CommentRequest commentRequest){
        Post post = postService.findById(commentRequest.getPostId());
        User currentUser = authService.getCurrentUser();

        commentService.createComment(Comment.builder()
                .user(currentUser)
                .date(Instant.now())
                .post(post)
                .contentType(contentTypeService.getContentTypeByValue(commentRequest.getContentType()))
                .content(commentRequest.getContent())
                .build());

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping("/{post_id}")
    public ResponseEntity<List<CommentResponse>> getCommentsByPost(@PathVariable Long postId){
        Post post = postService.findById(postId);
        User currentUser = authService.getCurrentUser();
        List<Comment> comments = commentService.findAllByPost(post);

        List<CommentResponse> commentResponses = commentService.canViewComments(comments, currentUser);

        return new ResponseEntity<>(commentResponses, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long id){
        Comment comment = commentService.findById(id);
        User curreUser = authService.getCurrentUser();
        if(comment != null && (comment.getUser().equals(curreUser) || comment.getPost().getUser().equals(curreUser))){
            commentService.delete(comment);
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }
}
