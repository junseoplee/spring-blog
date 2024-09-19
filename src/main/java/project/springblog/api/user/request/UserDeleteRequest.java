package project.springblog.api.user.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import project.springblog.application.user.request.UserDeleteServiceRequest;

@Getter
@NoArgsConstructor
public class UserDeleteRequest {

  @NotBlank(message = "이메일을 입력해주세요.")
  private String email;

  @NotBlank(message = "비밀번호를 입력해주세요.")
  private String password;

  @Builder
  private UserDeleteRequest(String email, String password) {
    this.email = email;
    this.password = password;
  }

  public UserDeleteServiceRequest toServiceRequest() {
    return UserDeleteServiceRequest.builder()
                                      .email(email)
                                      .password(password)
                                      .build();
  }
}

