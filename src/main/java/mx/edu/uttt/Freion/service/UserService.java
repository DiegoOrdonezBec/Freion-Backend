package mx.edu.uttt.Freion.service;

import mx.edu.uttt.Freion.dto.UserRequest;
import mx.edu.uttt.Freion.dto.UserResponse;
import mx.edu.uttt.Freion.model.Block;
import mx.edu.uttt.Freion.model.User;
import mx.edu.uttt.Freion.repository.BlockRepository;
import mx.edu.uttt.Freion.repository.FollowRepository;
import mx.edu.uttt.Freion.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
public class UserService {
    @Autowired
    private AuthService authService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BlockRepository blockRepository;

    @Autowired
    private FollowRepository followRepository;

    public ResponseEntity<List<UserResponse>> getAllUsers(){
        try{
            List<User> users = userRepository.findAll(Sort.by("username"));
            User currentUser = authService.getCurrentUser();
            List<UserResponse> userResponses = prepareUser(users, currentUser);
            return new ResponseEntity<>(userResponses, HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    public ResponseEntity<Void> blockByUsername(UserRequest userRequest){
        try{
            User blocked = userRepository.findByUsername(userRequest.getUsername()).orElse(null);
            User user = authService.getCurrentUser();
            if(!blockRepository.findByBlockerUsername(user.getUsername()).stream()
                    .map(block -> block.getBlocked())
                    .collect(Collectors.toList())
                    .contains(blocked)){
                blockRepository.save(Block.builder()
                        .blocker(user)
                        .blocked(blocked)
                        .build()
                );
            }
            return new ResponseEntity<>(HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    public ResponseEntity<List<UserResponse>> getBlocks(){
        try{
            User blocker = authService.getCurrentUser();
            List<UserResponse> blockResponses = blockRepository.findByBlocker(blocker, Sort.by("username")).stream()
                    .map(block -> {
                        return UserResponse.builder()
                                .id(block.getBlocked().getId())
                                .username(block.getBlocked().getUsername())
                                .profilePhotoUrl(block.getBlocked().getProfilePhotoUrl())
                                .build();
                    }).collect(Collectors.toList());

            return new ResponseEntity<>(blockResponses, HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    public ResponseEntity<List<UserResponse>> getFollowers(){
        try{
            User followed = authService.getCurrentUser();
            List<User> followers = followRepository.findByFollowed(followed, Sort.by("username")).stream()
                    .map(follow -> follow.getFollower())
                    .collect(Collectors.toList());
            List<UserResponse> followResponses = prepareUser(followers, followed);
            return new ResponseEntity<>(followResponses, HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    public List<UserResponse> prepareUser(List<User> users, User user){
        List<UserResponse> userResponses = users.stream()
                .filter(userResponse -> {
                    Boolean valid = true;
                    if(blockRepository.findByBlockerUsername(userResponse.getUsername()).stream()
                            .map(block -> block.getBlocked())
                            .collect(Collectors.toList())
                            .contains(user)){
                        valid = false;
                    }
                    if(blockRepository.findByBlockerUsername(user.getUsername()).stream()
                            .map(block -> block.getBlocked())
                            .collect(Collectors.toList())
                            .contains(userResponse)){
                        valid = false;
                    }
                    return valid;
                }).map(userResponse -> {
                        return UserResponse.builder()
                                .id(userResponse.getId())
                                .username(userResponse.getUsername())
                                .profilePhotoUrl(userResponse.getProfilePhotoUrl())
                                .build();
                }).collect(Collectors.toList());

        return userResponses;
    }
}
