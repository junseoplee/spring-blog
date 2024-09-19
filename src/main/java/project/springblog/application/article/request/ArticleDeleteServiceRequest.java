package project.springblog.application.article.request;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ArticleDeleteServiceRequest {

  private String email;
  private String password;

  @Builder
  private ArticleDeleteServiceRequest(String email, String password) {
    this.email = email;
    this.password = password;
  }
}
