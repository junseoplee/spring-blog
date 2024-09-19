package project.springblog.application.comment.request;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CommentDeleteServiceRequest {

  private String email;
  private String password;

  @Builder
  private CommentDeleteServiceRequest(String email, String password) {
    this.email = email;
    this.password = password;
  }
}
