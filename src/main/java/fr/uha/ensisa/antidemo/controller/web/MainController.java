package fr.uha.ensisa.antidemo.controller.web;

import fr.uha.ensisa.antidemo.dao.IArticleService;
import fr.uha.ensisa.antidemo.dao.IImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
@RequiredArgsConstructor
public class MainController {

  private final IArticleService articleService;
  private final IImageService imageService;


  @GetMapping("/")
  public String all(Model model) {
    return "redirect:/index";
  }


  @GetMapping("/index")
  public String index(Model model) {
    model.addAttribute("articles", articleService.getAll());
    return "index";
  }

  @GetMapping("/index/article/{id}")
  public String index(Model model, @PathVariable("id") Long id) {
    model.addAttribute("article", articleService.findByID(id));
    return "single";
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

}
