package project.springblog.api.article.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import project.springblog.application.article.request.ArticleCreateServiceRequest;

@Getter
@NoArgsConstructor
public class ArticleCreateRequest {

  @NotBlank(message = "이메일을 입력해주세요.")
  private String email;

  @NotBlank(message = "비밀번호를 입력해주세요.")
  private String password;

  @NotBlank(message = "제목을 입력해주세요.")
  private String title;

  @NotBlank(message = "내용을 입력해주세요.")
  private String content;

  @Builder
  private ArticleCreateRequest(String email, String password, String title, String content) {
    this.email = email;
    this.password = password;
    this.title = title;
    this.content = content;
  }

  public ArticleCreateServiceRequest toServiceRequest() {
    return ArticleCreateServiceRequest.builder()
                                      .email(email)
                                      .password(password)
                                      .title(title)
                                      .content(content)
                                      .build();
  }
}
