package fr.uha.ensisa.antidemo.controller.web;

import fr.uha.ensisa.antidemo.dao.IImageService;
import fr.uha.ensisa.antidemo.dto.FileCreationRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;
import java.io.IOException;
import java.sql.SQLException;

@Controller
@RequiredArgsConstructor
public class ImageScreenController {

  private final IImageService imageService;

  @ModelAttribute("image")
  public FileCreationRequestDto imageCreationRequestDto() {
    return new FileCreationRequestDto();
  }

  @GetMapping("/images")
  public String image(Model model) {
    model.addAttribute("images", imageService.getAll());
    return "images";
  }

  @GetMapping("/image/{id}")
  public String getPoster(@PathVariable("id") Long id
  ) throws SQLException {
    if (imageService.existsById(id)) {
      return "redirect:/image-exist/" +  id;
    }
    return "redirect:/images/upload.png";
  }

  @GetMapping("/static-image/{name}")
  public String getStaticImage(@PathVariable("name") String name) {
    return "redirect:/images/" + name;
  }

  @GetMapping("/image-exist/{id}")
  public ResponseEntity<byte[]> getExistingImage(@PathVariable("id") Long id
  ) throws SQLException {
    if (imageService.existsById(id)) {
      return ResponseEntity.ok().contentType(MediaType.IMAGE_PNG).body(imageService.getById(id).getData());
    }
    return ResponseEntity.notFound().build();
  }

  @PostMapping("/delete-image/{id}")
  public String deleteImage(@PathVariable("id") Long id) {
    imageService.deleteById(id);
    return "redirect:/images";
  }

  @PostMapping("/upload-image")
  public String createArticle(
    @ModelAttribute("image") @Valid FileCreationRequestDto imageDto) throws IOException {
    imageService.store(imageDto.getFile());
    return "redirect:/images";
  }


}
