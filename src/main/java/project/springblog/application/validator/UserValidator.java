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

  public User validateUserAndPassword(String email, String rawPassword) {
    User user = userRepository.findByEmail(email)
                              .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

    if (!passwordEncoder.matches(rawPassword, user.getPassword())) {
      throw new BusinessException(ErrorCode.WRONG_PASSWORD);
    }

    return user;
  }

  public void checkDuplicateEmail(String email) {
    if (userRepository.findByEmail(email).isPresent()) {
      throw new BusinessException(ErrorCode.DUPLICATE_MAIL);
    }
  }

  public String encodePassword(String rawPassword) {
    return passwordEncoder.encode(rawPassword);
  }
}
