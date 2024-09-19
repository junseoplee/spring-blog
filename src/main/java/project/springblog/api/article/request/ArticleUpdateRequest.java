package project.springblog.api.article.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import project.springblog.application.article.request.ArticleUpdateServiceRequest;

public class ArticleUpdateRequest {

  @NotBlank(message = "이메일을 입력해주세요.")
  private String email;

  @NotBlank(message = "비밀번호를 입력해주세요.")
  private String password;

  @NotBlank(message = "제목을 입력해주세요.")
  private String title;

  @NotBlank(message = "내용을 입력해주세요.")
  private String content;

  @Builder
  private ArticleUpdateRequest(String email, String password, String title, String content) {
    this.email = email;
    this.password = password;
    this.title = title;
    this.content = content;
  }

  public ArticleUpdateServiceRequest toServiceRequest() {
    return ArticleUpdateServiceRequest.builder()
                                      .email(email)
                                      .password(password)
                                      .title(title)
                                      .content(content)
                                      .build();
  }
}
