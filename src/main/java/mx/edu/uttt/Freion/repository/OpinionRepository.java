package mx.edu.uttt.Freion.repository;

import mx.edu.uttt.Freion.model.Opinion;
import mx.edu.uttt.Freion.model.Post;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OpinionRepository extends JpaRepository<Opinion, Long> {
    List<Opinion> findByPost(Post post, Sort sort);
    Long countByPost(Post post);
}
