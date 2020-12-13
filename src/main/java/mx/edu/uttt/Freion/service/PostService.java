package mx.edu.uttt.Freion.service;

import mx.edu.uttt.Freion.dto.PostRequest;
import mx.edu.uttt.Freion.dto.PostResponse;
import mx.edu.uttt.Freion.model.Post;
import mx.edu.uttt.Freion.model.User;
import mx.edu.uttt.Freion.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class PostService {
    @Autowired
    private PostRepository postRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private OpinionRepository opinionRepository;

    @Autowired
    private ViewRepository viewRepository;

    @Autowired
    private AuthService authService;

    @Autowired
    private ContentTypeRepository contentTypeRepository;

    @Autowired
    private PrivacyRepository privacyRepository;

    @Autowired
    private FollowRepository followRepository;

    @Autowired
    private BlockRepository blockRepository;

    public ResponseEntity<Void> createPost(PostRequest postRequest){
        try{
            Post post = Post.builder()
                    .user(authService.getCurrentUser())
                    .date(Instant.now())
                    .contentType(contentTypeRepository.findByValue(postRequest.getContentType()).orElse(null))
                    .content(postRequest.getContent())
                    .privacy(privacyRepository.findByValue(postRequest.getPrivacy()).orElse(null))
                    .build();

            postRepository.save(post);

            return new ResponseEntity<>(HttpStatus.CREATED);
        }catch(Exception e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    public ResponseEntity<List<PostResponse>> getAllPost(){
        try{
            List<Post> posts = postRepository.findAll();
            User currentUser = authService.getCurrentUser();
            List<PostResponse> postsResponses = preparePost(posts, currentUser);
            return new ResponseEntity<>(postsResponses, HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    public ResponseEntity<List<PostResponse>> getFollowPosts(){
        try{
            User currentUser = authService.getCurrentUser();
            List<User> follows = followRepository.findByFollowerUsername(currentUser.getUsername()).stream()
                    .map(follow -> follow.getFollowed())
                    .collect(Collectors.toList());
            List<Post> posts = postRepository.findByUserIn(follows, Sort.by("date").descending());
            List<PostResponse> postsResponses = preparePost(posts, currentUser);
            return new ResponseEntity<>(postsResponses, HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

    }

    public ResponseEntity<List<PostResponse>> getAllPostByUsername(String username){
        try{
            List<Post> posts = postRepository.findByUserUsername(username, Sort.by("date").descending());
            User currentUser = authService.getCurrentUser();
            List<PostResponse> postsResponses = preparePost(posts, currentUser);
            return new ResponseEntity<>(postsResponses, HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    public ResponseEntity<Void> deletePostById(Long id){
        try{
            Post post = postRepository.findById(id).orElse(null);
            if(post != null && post.getUser().equals(authService.getCurrentUser())){
                postRepository.deleteById(post.getId());
                return new ResponseEntity<>(HttpStatus.OK);
            }
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    private List<PostResponse> preparePost(List<Post> posts, User user){
        List<PostResponse> postsResponses = posts.stream()
                .filter(post -> {
                    boolean valid = true;
                    if(!post.getUser().equals(user)){
                        if(blockRepository.findByBlockerUsername(post.getUser().getUsername()).stream()
                                .map(block -> block.getBlocked())
                                .collect(Collectors.toList())
                                .contains(user)
                        ){
                            valid = false;
                        }
                        if(blockRepository.findByBlockedUsername(post.getUser().getUsername()).stream()
                                .map(block -> block.getBlocker())
                                .collect(Collectors.toList())
                                .contains(user)){
                            valid = false;
                        }
                        if(post.getPrivacy().getValue().equals("ONLY_FOLLOWERS")){
                            if(!followRepository.findByFollowedUsername(post.getUser().getUsername()).stream()
                                    .map(follow -> follow.getFollower())
                                    .collect(Collectors.toList())
                                    .contains(user)){
                                valid = false;
                            }
                        }
                        if(post.getPrivacy().getValue().equals("PRIVATE")){
                            valid = false;
                        }
                    }
                    return valid;
                })
                .map(post -> {
                    return PostResponse.builder()
                            .id(post.getId())
                            .userPhotoUrl(post.getUser().getProfilePhotoUrl())
                            .name(post.getUser().getName())
                            .username(post.getUser().getUsername())
                            .date(post.getDate())
                            .contentType(post.getContentType().getValue())
                            .content(post.getContent())
                            .privacy(post.getPrivacy().getValue())
                            .opinions(opinionRepository.countByPost(post))
                            .comments(commentRepository.countByPost(post))
                            .views(viewRepository.countByPost(post))
                            .build();
                }).collect(Collectors.toList());

        return postsResponses;
    }
}
