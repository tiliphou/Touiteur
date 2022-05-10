package fr.uha.ensisa.antidemo.controller.web;

import fr.uha.ensisa.antidemo.dao.IArticleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class MainController {

  private final IArticleService articleService;


  @GetMapping("/")
  public String all(Model model) {
    return "redirect:/articles";
  }

  @GetMapping("/articles")
  public String root(Model model) {
    model.addAttribute("articles", articleService.getAll());
    return "articles";
  }

  @GetMapping("/login")
  public String login(Model model) {
    return "login";
  }

  @GetMapping("/user")
  public String userIndex() {
    return "user/articles";
  }
}
