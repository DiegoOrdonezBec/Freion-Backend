package mx.edu.uttt.Freion.repository;

import mx.edu.uttt.Freion.model.Photo;
import mx.edu.uttt.Freion.model.User;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PhotoRepository extends JpaRepository<Photo, Long> {
    List<Photo> findByUser(User user, Sort sort);
}
