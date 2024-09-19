package project.springblog.api.article;

import jakarta.validation.Valid;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import project.springblog.api.article.request.ArticleCreateRequest;
import project.springblog.application.article.ArticleService;
import project.springblog.application.article.response.ArticleCreateResponse;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ArticleController {

  private final ArticleService articleService;

  @PostMapping("/articles")
  public ResponseEntity<ArticleCreateResponse> createArticle(@Valid @RequestBody ArticleCreateRequest request) {
    ArticleCreateResponse response = articleService.createArticle(request.toServiceRequest());
    URI location = URI.create("/api/articles/" + response.getArticleId());
    return ResponseEntity.created(location).body(response);
  }
}
