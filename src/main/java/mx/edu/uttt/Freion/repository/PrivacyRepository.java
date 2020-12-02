package mx.edu.uttt.Freion.repository;

import mx.edu.uttt.Freion.model.Privacy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PrivacyRepository extends JpaRepository<Privacy, Long> {
    Optional<Privacy> findByValue(String value);
}
