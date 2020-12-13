package mx.edu.uttt.Freion.repository;

import mx.edu.uttt.Freion.model.Follow;
import mx.edu.uttt.Freion.model.User;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FollowRepository extends JpaRepository<Follow, Long> {
    List<Follow> findByFollowerUsername(String username, Sort sort);
    List<Follow> findByFollowerUsername(String username);
    List<Follow> findByFollowedUsername(String username, Sort sort);
    List<Follow> findByFollowedUsername(String username);
    List<Follow> findByFollowed(User user, Sort sort);
}
