package fr.uha.ensisa.antidemo.dao;

import fr.uha.ensisa.antidemo.dto.ArticleCreationRequestDto;
import fr.uha.ensisa.antidemo.entity.Article;

import java.util.List;

public interface IArticleService {
  void save(Article article);
  List<Article> getAll();

  Article findByID(Long id);

  void update(Long id, Article article);
}
