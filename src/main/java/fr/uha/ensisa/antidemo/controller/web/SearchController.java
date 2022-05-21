package fr.uha.ensisa.antidemo.controller.web;

import com.fasterxml.jackson.databind.util.JSONPObject;
import fr.uha.ensisa.antidemo.dao.IArticleService;
import fr.uha.ensisa.antidemo.entity.Article;
import fr.uha.ensisa.antidemo.repository.ArticleRepository;
import lombok.RequiredArgsConstructor;
import netscape.javascript.JSObject;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.persistence.Entity;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RestController
@RequiredArgsConstructor
public class SearchController {

  private final IArticleService articleService;

  @GetMapping("/index/search/{text}")
  public ResponseEntity<List<Map<String, String>>> search(@PathVariable("text") String text) throws JSONException {
    List<Article> articles = articleService.getArticleByPartOfTitle(text);
    List<Map<String, String>> result = new ArrayList<>();
    for (Article article : articles) {
      Map<String, String> element = new HashMap();
      element.put("title", article.getTitle());
      element.put("content", article.getContent());
      element.put("posterID", article.getPosterId().toString());
      element.put("id", article.getId().toString());
      result.add(element);
    }
    return ResponseEntity.ok().body(result);
  }
}
