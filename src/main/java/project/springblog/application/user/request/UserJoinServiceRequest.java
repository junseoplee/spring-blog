package project.springblog.application.user.request;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserJoinServiceRequest {

  private String email;
  private String password;
  private String username;

  @Builder
  private UserJoinServiceRequest(String email, String password, String username) {
    this.email = email;
    this.password = password;
    this.username = username;
  }
}
