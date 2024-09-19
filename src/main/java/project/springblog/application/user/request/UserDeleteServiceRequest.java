package project.springblog.application.user.request;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserDeleteServiceRequest {

  private String email;
  private String password;

  @Builder
  private UserDeleteServiceRequest(String email, String password) {
    this.email = email;
    this.password = password;
  }
}
