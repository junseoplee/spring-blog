package project.springblog.api.user;

import jakarta.validation.Valid;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import project.springblog.api.user.request.UserJoinRequest;
import project.springblog.api.user.request.UserDeleteRequest;
import project.springblog.application.user.UserService;
import project.springblog.application.user.response.UserJoinResponse;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class UserController {

  private final UserService userService;

  @PostMapping("/join")
  public ResponseEntity<UserJoinResponse> join(@Valid @RequestBody UserJoinRequest request) {
    UserJoinResponse response = userService.joinUser(request.toServiceRequest());
    URI location = URI.create("/api/users/" + response.getEmail());
    return ResponseEntity.created(location).body(response);
  }

  @DeleteMapping("/delete")
  public ResponseEntity<Void> delete(@Valid @RequestBody UserDeleteRequest request) {
    userService.deleteUser(request.toServiceRequest());
    return ResponseEntity.noContent().build();
  }
}
