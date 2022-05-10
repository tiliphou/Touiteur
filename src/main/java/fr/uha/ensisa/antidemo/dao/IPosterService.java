package fr.uha.ensisa.antidemo.dao;

import fr.uha.ensisa.antidemo.entity.Poster;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface IPosterService {
  Poster store(MultipartFile file) throws IOException;
  Poster getPosterById(Long id);

  Poster getById(Long id);
}
