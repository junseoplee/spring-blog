package project.springblog.application.user;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.springblog.application.user.request.UserJoinServiceRequest;
import project.springblog.application.user.response.UserJoinResponse;
import project.springblog.domain.user.User;
import project.springblog.domain.user.repository.UserRepository;
import project.springblog.exception.BusinessException;
import project.springblog.exception.ErrorCode;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserService {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;

  @Transactional
  public UserJoinResponse joinUser(UserJoinServiceRequest request) {
    Optional<User> existUser = userRepository.findByEmail(request.getEmail());
    if (existUser.isPresent()) {
      throw new BusinessException(ErrorCode.DUPLICATE_MAIL);
    }

    User user = User.builder()
                    .email(request.getEmail())
                    .password(passwordEncoder.encode(request.getPassword()))
                    .username(request.getUsername())
                    .build();
    User savedUser = userRepository.save(user);

    return new UserJoinResponse(savedUser.getEmail(), savedUser.getUsername());
  }
}
