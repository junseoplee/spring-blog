package project.springblog.domain.article.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.springblog.domain.article.Article;

@Repository
public interface ArticleRepository extends JpaRepository<Article, Long> {
}
