package fr.uha.ensisa.antidemo.controller.web;

import fr.uha.ensisa.antidemo.dao.IArticleService;
import fr.uha.ensisa.antidemo.dao.IPosterService;
import fr.uha.ensisa.antidemo.dto.ArticleCreationRequestDto;
import fr.uha.ensisa.antidemo.entity.Article;
import fr.uha.ensisa.antidemo.entity.Poster;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;

@RequiredArgsConstructor
@Controller
@RequestMapping("/article")
public class ArticleWebController {

  private final IArticleService articleService;
  private final IPosterService posterService;

  @ModelAttribute("article")
  public ArticleCreationRequestDto articleCreationRequestDto() {
    return new ArticleCreationRequestDto();
  }

  @GetMapping
  public String showArticleCreationForm() {
    return "post";
  }

  @PostMapping
  public String createArticle(@ModelAttribute("article") @Valid ArticleCreationRequestDto articleDto) throws IOException {
    Poster poster = posterService.store(articleDto.getPoster());
    articleService.save(
      Article.builder()
        .posterId(poster.getId())
        .content(articleDto.getContent())
        .title(articleDto.getTitle())
      .build()
      );
    return "index";
  }

  @GetMapping(value = "/edit/{id}")
  public String showArticleCreationForm(@PathVariable("id") Long id, Model model) {
    model.addAttribute("currentArticle", articleService.findByID(id));
    return "article-edit";
  }
  @PostMapping("/update/{id}")
  public String updateArticle(@PathVariable("id") Long id,
                              @Valid ArticleCreationRequestDto articleDto) throws IOException {
    Poster poster = posterService.store(articleDto.getPoster());
    articleService.update(id,
      Article.builder()
        .title(articleDto.getTitle())
        .content(articleDto.getContent())
        .posterId(poster.getId())
        .build());
    return "redirect:/";
  }

}
