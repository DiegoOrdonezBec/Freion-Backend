package mx.edu.uttt.Freion.service;

import mx.edu.uttt.Freion.model.Privacy;
import mx.edu.uttt.Freion.repository.PrivacyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class PrivacyService {
    @Autowired
    private PrivacyRepository privacyRepository;

    public Privacy getPrivacyByValue(String value){
        return privacyRepository.findByValue(value).get();
    }

    public List<Privacy> findByValueNot(String value){
        return privacyRepository.findByValueNot(value);
    }
}
