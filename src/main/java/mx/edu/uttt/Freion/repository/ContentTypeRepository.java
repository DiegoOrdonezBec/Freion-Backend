package mx.edu.uttt.Freion.repository;

import mx.edu.uttt.Freion.model.ContentType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ContentTypeRepository extends JpaRepository<ContentType, Long> {
    Optional<ContentType> findByValue(String value);
}
