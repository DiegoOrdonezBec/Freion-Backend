package mx.edu.uttt.Freion.service;

import javafx.geometry.Pos;
import mx.edu.uttt.Freion.dto.PostResponse;
import mx.edu.uttt.Freion.model.Comment;
import mx.edu.uttt.Freion.model.Post;
import mx.edu.uttt.Freion.model.User;
import mx.edu.uttt.Freion.repository.CommentRepository;
import mx.edu.uttt.Freion.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
public class PostService {
    @Autowired
    private PostRepository postRepository;

    @Autowired
    private CommentRepository commentRepository;

    public void save(Post post){
        postRepository.save(post);
    }

    public List<Post> findAll(){
        return postRepository.findAll(Sort.by("date").descending());
    }

    public List<Post> findAllByFollows(Set<User> follows) {
        return postRepository.findAllByUserIn(follows, Sort.by("date").descending());
    };

    public Post findById(Long id){
        return postRepository.findById(id).get();
    }

    public void deleteById(Long id){
        postRepository.deleteById(id);
    }

    public List<Post> findByUser(User user){
        return postRepository.findTopByUser(user, Sort.by("date").descending());
    }

    public List<PostResponse> canView(List<Post> posts, User user){
        List<PostResponse> postsResponses = posts.stream()
                .filter(post -> {
                    Boolean canView = true;
                    if(post.getUser().getBlocks().contains(user) || user.getBlocks().contains(post.getUser())){
                        canView = false;
                    }
                    if(post.getPrivacy().getValue().equals("ONLY_FOLLOWERS")){
                        if(!user.getFollows().contains(post.getUser()) && !post.getUser().equals(user)){
                            canView = false;
                        }
                    }
                    if(post.getPrivacy().getValue().equals("PRIVATE")){
                        if(!post.getUser().equals(user)){
                            canView = false;
                        }
                    }
                    return canView;
                }).map(post -> {
                    return PostResponse.builder()
                            .id(post.getId())
                            .userPhotoUrl(post.getUser().getProfilePhotoUrl())
                            .name(post.getUser().getName())
                            .username(post.getUser().getUsername())
                            .date(post.getDate())
                            .contentType(post.getContentType().getValue())
                            .content(post.getContent())
                            .privacy(post.getPrivacy().getValue())
                            .opinions(new Long(0))
                            .comments(commentRepository.countByPost(post))
                            .views(post.getViewers().stream().count())
                            .build();
                }).collect(Collectors.toList());

        return postsResponses;
    }
}
