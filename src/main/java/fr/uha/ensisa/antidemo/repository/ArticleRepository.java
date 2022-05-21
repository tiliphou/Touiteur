package fr.uha.ensisa.antidemo.repository;

import fr.uha.ensisa.antidemo.entity.Article;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ArticleRepository extends JpaRepository<Article, Long> {

  @Query("select a from Article a where a.title = :text ")
  Optional<List<Article>> getArticleByPartOfTitle(@Param("text") String text);
}
