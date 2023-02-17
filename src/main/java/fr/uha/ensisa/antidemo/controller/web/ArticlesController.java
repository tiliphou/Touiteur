package fr.uha.ensisa.antidemo.controller.web;

import fr.uha.ensisa.antidemo.dao.IArticleService;
import fr.uha.ensisa.antidemo.dao.IImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
@RequestMapping("/articles")
public class ArticlesController {

  private final IArticleService articleService;

  @GetMapping
  public String articles(Model model) {
    model.addAttribute("articles", articleService.getAll());
    return "articles";
  }

  @GetMapping("/id/desc")
  public String idDesc(Model model) {
    model.addAttribute("articles", articleService.getAllByIdDesc());
    return "articles";
  }

  @GetMapping("/id/asc")
  public String idAsc(Model model) {
    model.addAttribute("articles", articleService.getAllByIdAsc());
    return "articles";
  }


  @GetMapping("/reads/desc")
  public String readsDesc(Model model) {
    model.addAttribute("articles", articleService.getAllByReadCountDesc());
    return "articles";
  }

  @GetMapping("/reads/asc")
  public String readsAsc(Model model) {
    model.addAttribute("articles", articleService.getAllByReadCountAsc());
    return "articles";
  }


  @GetMapping("/creationdate/desc")
  public String creationDateDesc(Model model) {
    model.addAttribute("articles", articleService.getAllByCreationDateDesc());
    return "articles";
  }

  @GetMapping("/creationdate/asc")
  public String creationDateAsc(Model model) {
    model.addAttribute("articles", articleService.getAllByCreationDateAsc());
    return "articles";
  }


  @GetMapping("/title/desc")
  public String titleDesc(Model model) {
    model.addAttribute("articles", articleService.getAllByTitleDesc());
    return "articles";
  }

  @GetMapping("/title/asc")
  public String titleAsc(Model model) {
    model.addAttribute("articles", articleService.getAllByTitleAsc());
    return "articles";
  }

}
