package mx.edu.uttt.Freion.service;

import mx.edu.uttt.Freion.model.Photo;
import mx.edu.uttt.Freion.repository.PhotoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PhotoService {
    @Autowired
    private PhotoRepository photoRepository;

    public Photo save(Photo photo){
        return photoRepository.save(photo);
    }
}
