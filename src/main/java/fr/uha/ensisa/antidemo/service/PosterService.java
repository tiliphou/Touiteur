package fr.uha.ensisa.antidemo.service;

import fr.uha.ensisa.antidemo.dao.IPosterService;
import fr.uha.ensisa.antidemo.entity.Poster;
import fr.uha.ensisa.antidemo.repository.PosterRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class PosterService implements IPosterService {

  private final PosterRepository posterRepository;

  @Override
  public Poster store(MultipartFile file) throws IOException {
    return posterRepository.save(Poster.builder()
      .name(StringUtils.cleanPath(file.getOriginalFilename()))
      .type(file.getContentType())
      .data(file.getBytes())
      .build());
  }

  @Override
  public Poster getPosterById(Long id) {
    return posterRepository.findById(id).get();
  }

  @Override
  public Poster getById(Long id) {
    return posterRepository.findById(id).get();
  }
}
