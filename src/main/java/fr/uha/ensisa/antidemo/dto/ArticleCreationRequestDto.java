package fr.uha.ensisa.antidemo.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class ArticleCreationRequestDto {
  private String title;
  private String content;
  private MultipartFile poster;
}
