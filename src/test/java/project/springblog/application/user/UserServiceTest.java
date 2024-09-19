package project.springblog.application.user;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;
import project.springblog.application.user.request.UserJoinServiceRequest;
import project.springblog.application.user.response.UserJoinResponse;
import project.springblog.domain.user.User;
import project.springblog.domain.user.repository.UserRepository;
import project.springblog.exception.BusinessException;
import project.springblog.exception.ErrorCode;

class UserServiceTest {

  @InjectMocks
  private UserService userService;

  @Mock
  private UserRepository userRepository;

  @Mock
  private PasswordEncoder passwordEncoder;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  @DisplayName("회원가입_성공_테스트")
  void 회원가입_성공_테스트() {
    UserJoinServiceRequest request = UserJoinServiceRequest.builder()
                                                           .email("junseoplee@outlook.com")
                                                           .password("junseoplee")
                                                           .username("junseoplee")
                                                           .build();

    when(userRepository.findByEmail("junseoplee@outlook.com")).thenReturn(Optional.empty());
    when(passwordEncoder.encode("junseoplee")).thenReturn("encodedPassword");

    User user = User.builder()
                    .email("junseoplee@outlook.com")
                    .password("encodedPassword")
                    .username("junseoplee")
                    .build();

    when(userRepository.save(any(User.class))).thenReturn(user);

    UserJoinResponse response = userService.joinUser(request);

    assertThat(response.getEmail()).isEqualTo("junseoplee@outlook.com");
    assertThat(response.getUsername()).isEqualTo("junseoplee");
    verify(userRepository).save(any(User.class));
  }

  @Test
  @DisplayName("중복_이메일_회원가입_실패_테스트")
  void 중복_이메일_회원가입_실패_테스트() {
    UserJoinServiceRequest request = UserJoinServiceRequest.builder()
                                                           .email("junseoplee@outlook.com")
                                                           .password("junseoplee")
                                                           .username("junseoplee")
                                                           .build();

    User existingUser = User.builder()
                            .email("junseoplee@outlook.com")
                            .password("encodedPassword")
                            .username("junseoplee")
                            .build();

    when(userRepository.findByEmail("junseoplee@outlook.com")).thenReturn(Optional.of(existingUser));

    assertThatThrownBy(() -> userService.joinUser(request))
        .isInstanceOf(BusinessException.class)
        .hasMessageContaining(ErrorCode.DUPLICATE_MAIL.getMessage());

    verify(userRepository, never()).save(any(User.class));
  }
}
