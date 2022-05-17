package fr.uha.ensisa.antidemo.service;

import fr.uha.ensisa.antidemo.dao.IVideoService;
import fr.uha.ensisa.antidemo.entity.Image;
import fr.uha.ensisa.antidemo.entity.Video;
import fr.uha.ensisa.antidemo.repository.VideoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class VideoService implements IVideoService {

  private final VideoRepository videoRepository;

  @Override
  public Video store(MultipartFile file) throws IOException {
    return videoRepository.save(Video.builder()
      .name(StringUtils.cleanPath(file.getOriginalFilename()))
      .type(file.getContentType())
      .data(file.getBytes())
      .build());
  }

  @Override
  public List<Video> getAll() {
    return videoRepository.findAll();
  }


  @Override
  public boolean existsById(Long id) {
    return videoRepository.existsById(id);
  }

  @Override
  public Video getById(Long id) {
    return videoRepository.findById(id).get();
  }

  @Override
  public void deleteById(Long id) {
    videoRepository.deleteById(id);
  }
}
