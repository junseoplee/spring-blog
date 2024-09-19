package project.springblog.api.article;

import jakarta.validation.Valid;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project.springblog.api.article.request.ArticleCreateRequest;
import project.springblog.api.article.request.ArticleUpdateRequest;
import project.springblog.application.article.ArticleService;
import project.springblog.application.article.response.ArticleCreateResponse;
import project.springblog.application.article.response.ArticleUpdateResponse;

@RestController
@RequestMapping("/api/articles")
@RequiredArgsConstructor
public class ArticleController {

  private final ArticleService articleService;

  private static final String ARTICLE_URI = "/api/articles/";

  @PostMapping
  public ResponseEntity<ArticleCreateResponse> createArticle(@Valid @RequestBody ArticleCreateRequest request) {
    ArticleCreateResponse response = articleService.createArticle(request.toServiceRequest());
    URI location = URI.create(ARTICLE_URI + response.getArticleId());
    return ResponseEntity.created(location).body(response);
  }

  @PatchMapping("/{articleID}")
  public ResponseEntity<ArticleUpdateResponse> updateArticle(@PathVariable Long articleID,
      @Valid @RequestBody ArticleUpdateRequest request) {
    ArticleUpdateResponse response = articleService.updateArticle(
        articleID,
        request.toServiceRequest()
    );
    return ResponseEntity.ok(response);
  }
}
