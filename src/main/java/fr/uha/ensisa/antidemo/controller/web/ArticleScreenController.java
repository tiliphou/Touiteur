package fr.uha.ensisa.antidemo.controller.web;

import fr.uha.ensisa.antidemo.dao.IArticleService;
import fr.uha.ensisa.antidemo.dao.IImageService;
import fr.uha.ensisa.antidemo.dto.ArticleCreationRequestDto;
import fr.uha.ensisa.antidemo.entity.Article;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;

@RequiredArgsConstructor
@Controller
@RequestMapping("/article")
public class ArticleScreenController {

  private final IArticleService articleService;
  private final IImageService posterService;

  @ModelAttribute("article")
  public ArticleCreationRequestDto articleCreationRequestDto() {
    return new ArticleCreationRequestDto();
  }


  @GetMapping
  public String showArticleCreationForm() {
    return "article";
  }

  @PostMapping
  public String createArticle(@ModelAttribute("article") @Valid ArticleCreationRequestDto articleDto) throws IOException {
    articleService.save(
      Article.builder()
        .posterId(articleDto.getPosterId())
        .content(articleDto.getContent())
        .title(articleDto.getTitle())
        .build()
    );
    return "redirect:/articles";
  }

  @GetMapping(value = "/edit/{id}")
  public String showArticleCreationForm(@PathVariable("id") Long id, Model model) {
    if (articleService.findByID(id) != null) {
      Article article = articleService.findByID(id);
      model.addAttribute("currentArticle",
        ArticleCreationRequestDto.builder()
          .posterId(article.getPosterId())
          .content(article.getContent())
          .title(article.getTitle())
          .build());
      model.addAttribute("id", id);
    }
    return "article-edit";
  }

  @PostMapping("/edit/{id}")
  public String updateArticle(@PathVariable("id") Long id,
                              @Valid ArticleCreationRequestDto articleDto) throws IOException {
    articleService.update(id,
      Article.builder()
        .title(articleDto.getTitle())
        .content(articleDto.getContent())
        .posterId(articleDto.getPosterId())
        .build());
    return "redirect:/articles";
  }
  @PostMapping("/article/delete/{id}")
  public String updateArticle(@PathVariable("id") Long id) {
    if (articleService.findByID(id) != null)
      articleService.deleteById(id);
    return "redirect:/articles";
  }

}
