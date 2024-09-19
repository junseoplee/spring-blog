package project.springblog.application.article.request;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ArticleUpdateServiceRequest {

  private String email;
  private String password;
  private String title;
  private String content;

  @Builder
  private ArticleUpdateServiceRequest(String email, String password, String title, String content) {
    this.email = email;
    this.password = password;
    this.title = title;
    this.content = content;
  }
}
