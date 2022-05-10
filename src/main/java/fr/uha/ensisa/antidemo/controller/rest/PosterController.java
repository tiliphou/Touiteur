package fr.uha.ensisa.antidemo.controller.rest;

import fr.uha.ensisa.antidemo.dao.IPosterService;
import fr.uha.ensisa.antidemo.entity.Poster;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.sql.SQLException;

@Controller
@RequiredArgsConstructor
public class PosterController {

  private final IPosterService posterService;

  @GetMapping("/poster/{id}")

  public ResponseEntity<byte[]> getPoster(@PathVariable("id") Long id) throws SQLException {
    Poster poster = posterService.getById(id);
    return ResponseEntity.ok().contentType(MediaType.IMAGE_PNG).body(poster.getData());
  }
}
