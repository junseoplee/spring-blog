package project.springblog.application.article.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ArticleCreateResponse {

  private final Long articleId;
  private final String email;
  private final String title;
  private final String content;
}
