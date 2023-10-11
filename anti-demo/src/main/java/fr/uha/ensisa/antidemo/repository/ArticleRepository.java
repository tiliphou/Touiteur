package fr.uha.ensisa.antidemo.repository;

import fr.uha.ensisa.antidemo.entity.Article;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface ArticleRepository extends JpaRepository<Article, Long> {

  @Query("select a from Article a where a.title = :text ")
  Optional<List<Article>> getArticleByPartOfTitle(@Param("text") String text);
  List<Article> findAllByOrderByIdDesc();
  List<Article> findAllByOrderByIdAsc();
  List<Article> findAllByOrderByTitleDesc();
  List<Article> findAllByOrderByTitleAsc();
  List<Article> findAllByOrderByReadCountDesc();
  List<Article> findAllByOrderByReadCountAsc();
  List<Article> findAllByOrderByCreationDateDesc();
  List<Article> findAllByOrderByCreationDateAsc();
  Optional<List<Article>> findArticleByTitleIsContainingIgnoreCase(@Param("text") String text);

  @Modifying
  @Transactional
  @Query("update Article a set a.readCount = a.readCount + :inc where a.id = :id")
  void incrementReads(@Param("id") long id, @Param("inc") long inc);
}
