package fr.uha.ensisa.antidemo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ArticleCreationRequestDto {
  private String title;
  private String content;
  private Long posterId;
}
