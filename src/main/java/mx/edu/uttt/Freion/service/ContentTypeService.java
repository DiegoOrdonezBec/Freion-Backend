package mx.edu.uttt.Freion.service;

import mx.edu.uttt.Freion.model.ContentType;
import mx.edu.uttt.Freion.repository.ContentTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ContentTypeService {
    @Autowired
    private ContentTypeRepository contentTypeRepository;

    public ContentType getContentTypeByValue(String value){
        return contentTypeRepository.findByValue(value).get();
    }
}
