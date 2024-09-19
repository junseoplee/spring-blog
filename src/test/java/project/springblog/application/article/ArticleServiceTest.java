package project.springblog.application.article;

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
import project.springblog.application.article.request.ArticleCreateServiceRequest;
import project.springblog.application.article.response.ArticleCreateResponse;
import project.springblog.domain.article.Article;
import project.springblog.domain.article.repository.ArticleRepository;
import project.springblog.domain.user.User;
import project.springblog.domain.user.repository.UserRepository;
import project.springblog.exception.BusinessException;
import project.springblog.exception.ErrorCode;

class ArticleServiceTest {

  @InjectMocks
  private ArticleService articleService;

  @Mock
  private ArticleRepository articleRepository;

  @Mock
  private UserRepository userRepository;

  @Mock
  private PasswordEncoder passwordEncoder;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  @DisplayName("게시물_생성_성공_테스트")
  void 게시물_생성_성공_테스트() {
    ArticleCreateServiceRequest request = ArticleCreateServiceRequest.builder()
                                                                     .email("junseoplee@outlook.com")
                                                                     .password("junseoplee")
                                                                     .title("junseoplee")
                                                                     .content("junseoplee")
                                                                     .build();

    User user = User.builder()
                    .email("junseoplee@outlook.com")
                    .password("junseoplee")
                    .username("junseoplee")
                    .build();

    Article article = Article.builder()
                             .user(user)
                             .title("junseoplee")
                             .content("junseoplee")
                             .build();

    when(userRepository.findByEmail("junseoplee@outlook.com")).thenReturn(Optional.of(user));
    when(passwordEncoder.matches("junseoplee", "junseoplee")).thenReturn(true);
    when(articleRepository.save(any(Article.class))).thenReturn(article);

    ArticleCreateResponse response = articleService.createArticle(request);

    assertThat(response.getEmail()).isEqualTo("junseoplee@outlook.com");
    assertThat(response.getTitle()).isEqualTo("junseoplee");
    assertThat(response.getContent()).isEqualTo("junseoplee");
    verify(articleRepository).save(any(Article.class));
  }

  @Test
  @DisplayName("게시물_생성_실패_잘못된_비밀번호")
  void 게시물_생성_실패_잘못된_비밀번호() {
    ArticleCreateServiceRequest request = ArticleCreateServiceRequest.builder()
                                                                     .email("junseoplee@outlook.com")
                                                                     .password("junseoplee")
                                                                     .title("junseoplee")
                                                                     .content("junseoplee")
                                                                     .build();

    User user = User.builder()
                    .email("junseoplee@outlook.com")
                    .password("junseoplee")
                    .username("junseoplee")
                    .build();

    when(userRepository.findByEmail("junseoplee@outlook.com")).thenReturn(Optional.of(user));
    when(passwordEncoder.matches("junseoplee", "junseoplee")).thenReturn(false);

    assertThatThrownBy(() -> articleService.createArticle(request))
        .isInstanceOf(BusinessException.class)
        .hasMessageContaining(ErrorCode.WRONG_PASSWORD.getMessage());

    verify(articleRepository, never()).save(any(Article.class));
  }

  @Test
  @DisplayName("게시물_생성_실패_존재하지_않는_사용자")
  void 게시물_생성_실패_존재하지_않는_사용자() {
    ArticleCreateServiceRequest request = ArticleCreateServiceRequest.builder()
                                                                     .email("junseoplee@outlook.com")
                                                                     .password("junseoplee")
                                                                     .title("junseoplee")
                                                                     .content("junseoplee")
                                                                     .build();

    when(userRepository.findByEmail("junseoplee@outlook.com")).thenReturn(Optional.empty());

    assertThatThrownBy(() -> articleService.createArticle(request))
        .isInstanceOf(BusinessException.class)
        .hasMessageContaining(ErrorCode.USER_NOT_FOUND.getMessage());

    verify(articleRepository, never()).save(any(Article.class));
  }
}
