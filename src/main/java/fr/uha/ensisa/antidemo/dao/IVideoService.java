package fr.uha.ensisa.antidemo.dao;

import fr.uha.ensisa.antidemo.entity.Image;
import fr.uha.ensisa.antidemo.entity.Video;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface IVideoService {
  Video store(MultipartFile file) throws IOException;
  List<Video> getAll();
  boolean existsById(Long id);
  Video getById(Long id);
  void deleteById(Long id);
}
