package project.springblog.api.article.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import project.springblog.application.article.request.ArticleDeleteServiceRequest;

@Getter
@NoArgsConstructor
public class ArticleDeleteRequest {

  @NotBlank(message = "이메일을 입력해주세요.")
  private String email;

  @NotBlank(message = "비밀번호를 입력해주세요.")
  private String password;

  @Builder
  private ArticleDeleteRequest(String email, String password) {
    this.email = email;
    this.password = password;
  }

  public ArticleDeleteServiceRequest toServiceRequest() {
    return ArticleDeleteServiceRequest.builder()
                                      .email(email)
                                      .password(password)
                                      .build();
  }
}
