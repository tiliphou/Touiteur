package fr.uha.ensisa.antidemo.repository;

import fr.uha.ensisa.antidemo.entity.Article;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArticleRepository extends JpaRepository<Article, Long> {

}
