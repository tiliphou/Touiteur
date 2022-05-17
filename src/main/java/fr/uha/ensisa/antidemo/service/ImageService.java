package fr.uha.ensisa.antidemo.service;

import fr.uha.ensisa.antidemo.dao.IImageService;
import fr.uha.ensisa.antidemo.entity.Image;
import fr.uha.ensisa.antidemo.repository.ImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ImageService implements IImageService {

  private final ImageRepository imageRepository;

  @Override
  public Image store(MultipartFile file) throws IOException {
    return imageRepository.save(Image.builder()
      .name(StringUtils.cleanPath(file.getOriginalFilename()))
      .type(file.getContentType())
      .data(file.getBytes())
      .build());
  }

  @Override
  public List<Image> getAll() {
    return imageRepository.findAll();
  }


  @Override
  public boolean existsById(Long id) {
    return imageRepository.existsById(id);
  }

  @Override
  public Image getById(Long id) {
    return imageRepository.findById(id).get();
  }

  @Override
  public void deleteById(Long id) {
    imageRepository.deleteById(id);
  }
}
