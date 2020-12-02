package mx.edu.uttt.Freion.repository;

import mx.edu.uttt.Freion.model.Post;
import mx.edu.uttt.Freion.model.User;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findAll(Sort sort);
    List<Post> findTopByUser(User user);
}
