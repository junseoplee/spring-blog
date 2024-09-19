package project.springblog.api.comment.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import project.springblog.application.comment.request.CommentDeleteServiceRequest;

@Getter
@NoArgsConstructor
public class CommentDeleteRequest {

  @NotBlank(message = "이메일을 입력해주세요.")
  private String email;

  @NotBlank(message = "비밀번호를 입력해주세요.")
  private String password;

  @Builder
  private CommentDeleteRequest(String email, String password) {
    this.email = email;
    this.password = password;
  }

  public CommentDeleteServiceRequest toServiceRequest() {
    return CommentDeleteServiceRequest.builder()
                                      .email(email)
                                      .password(password)
                                      .build();
  }
}
