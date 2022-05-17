package fr.uha.ensisa.antidemo.controller.web;

import fr.uha.ensisa.antidemo.dao.IVideoService;
import fr.uha.ensisa.antidemo.dto.FileCreationRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.MediaTypeFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;
import java.net.MalformedURLException;
import java.sql.SQLException;

@Controller
@RequiredArgsConstructor
public class VideoScreenController {

  private final IVideoService videoService;

  @ModelAttribute("video")
  public FileCreationRequestDto imageCreationRequestDto() {
    return new FileCreationRequestDto();
  }

  @GetMapping("/videos")
  public String video(Model model) {
    model.addAttribute("videos", videoService.getAll());
    return "videos";
  }

  @GetMapping("/video/{id}")
  public String getVideoById(@PathVariable("id") Long id
  ) throws SQLException {
    if (videoService.existsById(id)) {
      return "redirect:/video-exist/" + id;
    }
    return "redirect:/images/upload.png";
  }

  @GetMapping("/video/embed/{id}")
  public ResponseEntity<Resource> getEmbedVideo(@PathVariable("id") Long id)  {
    return ResponseEntity
      .ok(new ByteArrayResource(videoService.getById(id).getData()));

  }
  @GetMapping("video-static/{name}")
  public String getStaticVideo(@PathVariable("name") String name) {
    return "redirect:/images/" + name;
  }

  @GetMapping("/video-exist/{id}")
  public ResponseEntity<byte[]> getExistingVideo(@PathVariable("id") Long id
  ) throws SQLException {
    if (videoService.existsById(id)) {
      return ResponseEntity.ok().body(videoService.getById(id).getData());
    }
    return ResponseEntity.notFound().build();
  }

  @PostMapping("/delete-video/{id}")
  public String deleteVideoById(@PathVariable("id") Long id) {
    videoService.deleteById(id);
    return "redirect:/videos";
  }

  @PostMapping("/upload-video")
  public String createVideo(
    @ModelAttribute("video") @Valid FileCreationRequestDto videoDto) throws IOException {
    videoService.store(videoDto.getFile());
    return "redirect:/videos";
  }


}
