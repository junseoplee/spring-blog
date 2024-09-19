package project.springblog.api.user.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import project.springblog.application.user.request.UserJoinServiceRequest;

@Getter
@NoArgsConstructor
public class UserJoinRequest {

  @NotBlank(message = "이메일을 입력해주세요.")
  private String email;

  @NotBlank(message = "비밀번호를 입력해주세요.")
  private String password;

  @NotBlank(message = "이름을 입력해주세요.")
  private String username;

  @Builder
  private UserJoinRequest(String email, String password, String username) {
    this.email = email;
    this.password = password;
    this.username = username;
  }

  public UserJoinServiceRequest toServiceRequest() {
    return UserJoinServiceRequest.builder()
                                 .email(email)
                                 .password(password)
                                 .username(username)
                                 .build();
  }
}
