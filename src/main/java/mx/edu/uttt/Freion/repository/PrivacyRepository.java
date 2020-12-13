package mx.edu.uttt.Freion.repository;

import mx.edu.uttt.Freion.model.Privacy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface PrivacyRepository extends JpaRepository<Privacy, Long> {
    Optional<Privacy> findByValue(String value);
    List<Privacy> findByValueNot(String value);
}
