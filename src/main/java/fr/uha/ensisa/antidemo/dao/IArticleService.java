package fr.uha.ensisa.antidemo.dao;

import fr.uha.ensisa.antidemo.entity.Article;

import java.util.List;

public interface IArticleService {
  void save(Article article);

  List<Article> getAll();

  List<Article> getAllByReadCount();

  List<Article> getAllByCreationDate();

  Article findByID(Long id);

  void update(Long id, Article article);

  void deleteById(Long id);

  List<Article> getArticleByPartOfTitle(String text);

  void incrementReads(long id, long incr);

}
