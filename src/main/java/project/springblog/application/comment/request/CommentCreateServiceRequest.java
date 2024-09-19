package project.springblog.application.comment.request;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CommentCreateServiceRequest {

  private String email;
  private String password;
  private String content;

  @Builder
  private CommentCreateServiceRequest(String email, String password, String content) {
    this.email = email;
    this.password = password;
    this.content = content;
  }
}
