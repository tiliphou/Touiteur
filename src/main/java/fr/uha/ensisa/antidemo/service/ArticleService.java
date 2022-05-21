package fr.uha.ensisa.antidemo.service;

import fr.uha.ensisa.antidemo.dao.IArticleService;
import fr.uha.ensisa.antidemo.dto.ArticleCreationRequestDto;
import fr.uha.ensisa.antidemo.entity.Article;
import fr.uha.ensisa.antidemo.repository.ArticleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class ArticleService implements IArticleService {

  private final ArticleRepository articleRepository;
  @Override
  public void save(Article article) {
    articleRepository.save(
      Article.builder()
        .title(article.getTitle())
        .content(article.getContent())
        .posterId(article.getPosterId())
        .build()
    );
  }

  @Override
  public List<Article> getAll() {
    return articleRepository.findAll();
  }

  @Override
  public Article findByID(Long id) {
    return articleRepository.findById(id).get();
  }

  @Override
  public void update(Long id, Article article) {
    Article articleFound =articleRepository.findById(id).get();
    articleFound.setContent(article.getContent());
    articleFound.setTitle(article.getTitle());
    articleFound.setPosterId(article.getPosterId());
  }

  @Override
  public void deleteById(Long id) {
    articleRepository.deleteById(id);
  }

  @Override
  public List<Article> getArticleByPartOfTitle(String text) {
    Optional<List<Article>> articles = articleRepository.getArticleByPartOfTitle(text);
    if (articles == null) return  null;
    return articles.get();
  }
}
