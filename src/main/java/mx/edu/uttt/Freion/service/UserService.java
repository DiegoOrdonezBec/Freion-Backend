package mx.edu.uttt.Freion.service;

import mx.edu.uttt.Freion.dto.UserResponse;
import mx.edu.uttt.Freion.model.User;
import mx.edu.uttt.Freion.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public User save(User user){
        return userRepository.save(user);
    }

    public List<User> findAll(){return userRepository.findAll();}

    public Boolean existsByUsername(String username){
        return userRepository.existsByUsername(username);
    }

    public Boolean existsByEmail(String email){
        return userRepository.existsByEmail(email);
    }

    public User getByUsername(String username){
        return userRepository.findByUsername(username).get();
    }

    public List<UserResponse> canViewUsers(List<User> users, User user){
        List<UserResponse> userResponses = users.stream()
                .filter(userResponse -> {
                    Boolean canView = true;
                    if(userResponse.getBlocks().contains(user) || user.getBlocks().contains(userResponse)){
                        canView = false;
                    }

                    return canView;
                }).map(userResponse -> {
                        return UserResponse.builder()
                                .id(userResponse.getId())
                                .username(userResponse.getUsername())
                                .build();
                }).collect(Collectors.toList());

        return userResponses;
    }
}
