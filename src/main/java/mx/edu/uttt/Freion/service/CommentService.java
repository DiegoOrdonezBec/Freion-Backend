package mx.edu.uttt.Freion.service;

import mx.edu.uttt.Freion.dto.CommentResponse;
import mx.edu.uttt.Freion.dto.PostResponse;
import mx.edu.uttt.Freion.model.Comment;
import mx.edu.uttt.Freion.model.Post;
import mx.edu.uttt.Freion.model.User;
import mx.edu.uttt.Freion.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommentService {
    @Autowired
    private CommentRepository commentRepository;

    public void createComment(Comment comment){
        commentRepository.save(comment);
    }

    public Comment findById(Long id){
        return commentRepository.findById(id).get();
    }

    public void delete(Comment comment){
        commentRepository.delete(comment);
    }

    public List<Comment> findAllByPost(Post post){
        return commentRepository.findAllByPost(post);
    }

    public List<CommentResponse> canViewComments(List<Comment> comments, User user){
        List<CommentResponse>commentResponses = comments.stream()
                .filter(comment -> {
                    Boolean canView = true;
                    if(comment.getUser().getBlocks().contains(user) || user.getBlocks().contains(comment.getUser())){
                        canView = false;
                    }
                    return canView;
                }).map(comment -> {
                    return CommentResponse.builder()
                            .id(comment.getId())
                            .username(comment.getUser().getUsername())
                            .date(comment.getDate())
                            .contentType(comment.getContentType().getValue())
                            .content(comment.getContent())
                            .build();
                }).collect(Collectors.toList());

        return commentResponses;
    }
}
