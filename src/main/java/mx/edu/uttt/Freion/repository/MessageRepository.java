package mx.edu.uttt.Freion.repository;

import mx.edu.uttt.Freion.model.Conversation;
import mx.edu.uttt.Freion.model.Message;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {
    List<Message> findByConversation(Conversation conversation, Sort sort);
}
