package mx.edu.uttt.Freion.repository;

import mx.edu.uttt.Freion.model.Post;
import mx.edu.uttt.Freion.model.View;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ViewRepository extends JpaRepository<View, Long> {
    Long countByPost(Post post);
}
