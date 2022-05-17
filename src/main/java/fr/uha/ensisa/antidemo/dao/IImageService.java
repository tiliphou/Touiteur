package fr.uha.ensisa.antidemo.dao;

import fr.uha.ensisa.antidemo.entity.Image;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface IImageService {
  Image store(MultipartFile file) throws IOException;
  List<Image> getAll();
  boolean existsById(Long id);
  Image getById(Long id);
  void deleteById(Long id);
}
