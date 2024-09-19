package project.springblog.api.comment.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import project.springblog.application.comment.request.CommentCreateServiceRequest;

@Getter
@NoArgsConstructor
public class CommentCreateRequest {

  @NotBlank(message = "이메일을 입력해주세요.")
  private String email;

  @NotBlank(message = "비밀번호를 입력해주세요.")
  private String password;

  @NotBlank(message = "내용을 입력해주세요.")
  private String content;

  @Builder
  private CommentCreateRequest(String email, String password, String content) {
    this.email = email;
    this.password = password;
    this.content = content;
  }

  public CommentCreateServiceRequest toServiceRequest() {
    return CommentCreateServiceRequest.builder()
                                      .email(email)
                                      .password(password)
                                      .content(content)
                                      .build();
  }
}
