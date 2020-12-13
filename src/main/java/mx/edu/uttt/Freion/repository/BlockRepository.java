package mx.edu.uttt.Freion.repository;

import mx.edu.uttt.Freion.model.Block;
import mx.edu.uttt.Freion.model.User;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BlockRepository extends JpaRepository<Block, Long> {
    List<Block> findByBlockerUsername(String username, Sort sort);
    List<Block> findByBlockerUsername(String username);
    List<Block> findByBlockedUsername(String username, Sort sort);
    List<Block> findByBlockedUsername(String username);
    List<Block> findByBlocker(User user, Sort sort);
}
