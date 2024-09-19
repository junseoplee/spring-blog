package project.springblog.application.user;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import project.springblog.application.user.request.UserDeleteServiceRequest;
import project.springblog.application.user.request.UserJoinServiceRequest;
import project.springblog.application.user.response.UserJoinResponse;
import project.springblog.application.validator.UserValidator;
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
  private UserValidator userValidator;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  @DisplayName("회원가입 성공 테스트")
  void 회원가입_성공_테스트() {
    UserJoinServiceRequest request = UserJoinServiceRequest.builder()
                                                           .email("test@test.com")
                                                           .password("test-pw")
                                                           .username("test")
                                                           .build();

    when(userValidator.encodePassword("test-pw")).thenReturn("encodedPassword");

    User user = User.builder()
                    .email("test@test.com")
                    .password("encodedPassword")
                    .username("test")
                    .build();

    when(userRepository.save(any(User.class))).thenReturn(user);

    UserJoinResponse response = userService.joinUser(request);

    verify(userRepository).save(any(User.class));
  }

  @Test
  @DisplayName("회원 탈퇴 성공 테스트")
  void 회원탈퇴_성공_테스트() {
    UserDeleteServiceRequest request = UserDeleteServiceRequest.builder()
                                                               .email("test@test.com")
                                                               .password("test-pw")
                                                               .build();

    User user = User.builder()
                    .email("test@test.com")
                    .password("encodedPassword")
                    .username("test")
                    .build();

    when(userValidator.validateUserAndPassword("test@test.com", "test-pw")).thenReturn(user);

    userService.deleteUser(request);

    verify(userRepository).deleteById(user.getId());
  }

  @Test
  @DisplayName("중복 이메일로 인한 회원가입 실패 테스트")
  void 중복_이메일_회원가입_실패_테스트() {
    UserJoinServiceRequest request = UserJoinServiceRequest.builder()
                                                           .email("test@test.com")
                                                           .password("test-pw")
                                                           .username("test")
                                                           .build();

    doThrow(new BusinessException(ErrorCode.DUPLICATE_MAIL))
        .when(userValidator).checkDuplicateEmail("test@test.com");

    assertThatThrownBy(() -> userService.joinUser(request))
        .isInstanceOf(BusinessException.class)
        .hasMessageContaining(ErrorCode.DUPLICATE_MAIL.getMessage());

    verify(userRepository, never()).save(any(User.class));
  }

  @Test
  @DisplayName("회원 탈퇴 실패 - 존재하지 않는 사용자 테스트")
  void 회원탈퇴_실패_존재하지않는사용자_테스트() {
    UserDeleteServiceRequest request = UserDeleteServiceRequest.builder()
                                                               .email("test@test.com")
                                                               .password("test-pw")
                                                               .build();

    when(userValidator.validateUserAndPassword("test@test.com", "test-pw"))
        .thenThrow(new BusinessException(ErrorCode.USER_NOT_FOUND));

    assertThatThrownBy(() -> userService.deleteUser(request))
        .isInstanceOf(BusinessException.class)
        .hasMessageContaining(ErrorCode.USER_NOT_FOUND.getMessage());
  }
}
