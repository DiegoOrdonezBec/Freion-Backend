package mx.edu.uttt.Freion.repository;

import mx.edu.uttt.Freion.model.Post;
import mx.edu.uttt.Freion.model.Privacy;
import mx.edu.uttt.Freion.model.User;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findAll(Sort sort);
    List<Post> findByUser(User user, Sort sort);
    List<Post> findByUserUsername(String username, Sort sort);
    List<Post> findByUserIn(List<User> follows, Sort sort);

}
