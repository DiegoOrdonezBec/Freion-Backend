package mx.edu.uttt.Freion.repository;

import mx.edu.uttt.Freion.model.Conversation;
import mx.edu.uttt.Freion.model.User;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ConversationRepository extends JpaRepository<Conversation, Long> {
    List<Conversation> findByMembers(User user, Sort sort);
}
