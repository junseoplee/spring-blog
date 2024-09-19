package project.springblog.application.validator;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import project.springblog.domain.user.User;
import project.springblog.domain.user.repository.UserRepository;
import project.springblog.exception.BusinessException;
import project.springblog.exception.ErrorCode;

@Component
@RequiredArgsConstructor
public class UserValidator {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;

  public User validateUserAndPassword(String email, String password) {
    User existUser = userRepository.findByEmail(email)
                                   .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

    if (!passwordEncoder.matches(password, existUser.getPassword())) {
      throw new BusinessException(ErrorCode.WRONG_PASSWORD);
    }

    return existUser;
  }
}
