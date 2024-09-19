package project.springblog.application.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.springblog.application.user.request.UserJoinServiceRequest;
import project.springblog.application.user.request.UserDeleteServiceRequest;
import project.springblog.application.user.response.UserJoinResponse;
import project.springblog.application.validator.UserValidator;
import project.springblog.domain.user.User;
import project.springblog.domain.user.repository.UserRepository;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserService {

  private final UserRepository userRepository;
  private final UserValidator userValidator;

  @Transactional
  public UserJoinResponse joinUser(UserJoinServiceRequest request) {
    userValidator.checkDuplicateEmail(request.getEmail());

    User user = User.builder()
                    .email(request.getEmail())
                    .password(userValidator.encodePassword(request.getPassword()))
                    .username(request.getUsername())
                    .build();

    User savedUser = userRepository.save(user);
    return new UserJoinResponse(savedUser.getEmail(), savedUser.getUsername());
  }

  @Transactional
  public void deleteUser(UserDeleteServiceRequest request) {
    User existUser = userValidator.validateUserAndPassword(request.getEmail(), request.getPassword());
    userRepository.deleteById(existUser.getId());
  }
}
