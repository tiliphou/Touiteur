package fr.uha.ensisa.antidemo.controller.web;

import fr.uha.ensisa.antidemo.dao.IArticleService;
import fr.uha.ensisa.antidemo.dao.IImageService;
import fr.uha.ensisa.antidemo.dto.ArticleCreationRequestDto;
import fr.uha.ensisa.antidemo.entity.Article;
import fr.uha.ensisa.antidemo.entity.Image;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;
import java.io.IOException;

@Controller
@RequiredArgsConstructor
public class MainController {

  private final IArticleService articleService;
  private final IImageService imageService;


  @GetMapping("/")
  public String all(Model model) {
    return "redirect:/articles";
  }

  @GetMapping("/articles")
  public String articles(Model model) {
    model.addAttribute("articles", articleService.getAll());
    return "articles";
  }



  @GetMapping("/login")
  public String login(Model model) {
    return "authentication";
  }

  @GetMapping("/user")
  public String userIndex() {
    return "user/articles";
  }
}
