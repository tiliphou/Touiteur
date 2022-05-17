package fr.uha.ensisa.antidemo.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotEmpty;

@Data
public class FileCreationRequestDto {
  @NotEmpty
  private MultipartFile file;

}
