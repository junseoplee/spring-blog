package project.springblog.application.article;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import project.springblog.application.article.request.ArticleDeleteServiceRequest;
import project.springblog.application.article.request.ArticleUpdateServiceRequest;
import project.springblog.application.article.response.ArticleUpdateResponse;
import project.springblog.application.validator.UserValidator;
import project.springblog.domain.article.Article;
import project.springblog.domain.article.repository.ArticleRepository;
import project.springblog.domain.user.User;
import project.springblog.exception.BusinessException;
import project.springblog.exception.ErrorCode;
import project.springblog.application.article.request.ArticleCreateServiceRequest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.*;

class ArticleServiceTest {

  @Mock
  private ArticleRepository articleRepository;

  @Mock
  private UserValidator userValidator;

  @InjectMocks
  private ArticleService articleService;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  @DisplayName("글 작성 실패 - 존재하지 않는 사용자")
  void 글작성_실패_존재하지않는사용자() {
    ArticleCreateServiceRequest request = ArticleCreateServiceRequest.builder()
                                                                     .email("test@test.com")
                                                                     .password("wrongPassword")
                                                                     .title("test")
                                                                     .content("test")
                                                                     .build();

    doThrow(new BusinessException(ErrorCode.USER_NOT_FOUND))
        .when(userValidator).validateUserAndPassword("test@test.com", "wrongPassword");

    assertThatThrownBy(() -> articleService.createArticle(request))
        .isInstanceOf(BusinessException.class)
        .hasMessageContaining(ErrorCode.USER_NOT_FOUND.getMessage());

    verify(articleRepository, never()).save(any(Article.class));
  }

  @Test
  @DisplayName("글 작성 실패 - 잘못된 비밀번호")
  void 글작성_실패_잘못된비밀번호() {
    ArticleCreateServiceRequest request = ArticleCreateServiceRequest.builder()
                                                                     .email("test@test.com")
                                                                     .password("wrongPassword")
                                                                     .title("test")
                                                                     .content("test")
                                                                     .build();

    User user = User.builder()
                    .email("test@test.com")
                    .password("encodedCorrectPassword")
                    .username("testUser")
                    .build();

    when(userValidator.validateUserAndPassword("test@test.com", "wrongPassword"))
        .thenThrow(new BusinessException(ErrorCode.WRONG_PASSWORD));

    assertThatThrownBy(() -> articleService.createArticle(request))
        .isInstanceOf(BusinessException.class)
        .hasMessageContaining(ErrorCode.WRONG_PASSWORD.getMessage());

    verify(articleRepository, never()).save(any(Article.class));
  }

  @Test
  @DisplayName("글 작성 성공")
  void 글작성_성공() {
    ArticleCreateServiceRequest request = ArticleCreateServiceRequest.builder()
                                                                     .email("test@test.com")
                                                                     .password("test-pw")
                                                                     .title("test")
                                                                     .content("test content")
                                                                     .build();

    User user = User.builder()
                    .email("test@test.com")
                    .password("encodedPassword")
                    .username("testUser")
                    .build();

    Article article = Article.builder()
                             .user(user)
                             .title("test")
                             .content("test content")
                             .build();

    when(userValidator.validateUserAndPassword("test@test.com", "test-pw")).thenReturn(user);
    when(articleRepository.save(any(Article.class))).thenReturn(article);

    articleService.createArticle(request);

    verify(articleRepository).save(any(Article.class));
  }

  @Test
  @DisplayName("글 수정 실패 - 존재하지 않는 사용자")
  void 글수정_실패_존재하지않는사용자() {
    ArticleUpdateServiceRequest request = ArticleUpdateServiceRequest.builder()
                                                                     .email("nonexistent@test.com")
                                                                     .password("wrongPassword")
                                                                     .title("updatedTitle")
                                                                     .content("updatedContent")
                                                                     .build();

    doThrow(new BusinessException(ErrorCode.USER_NOT_FOUND))
        .when(userValidator).validateUserAndPassword("nonexistent@test.com", "wrongPassword");

    assertThatThrownBy(() -> articleService.updateArticle(1L, request))
        .isInstanceOf(BusinessException.class)
        .hasMessageContaining(ErrorCode.USER_NOT_FOUND.getMessage());

    verify(articleRepository, never()).save(any(Article.class));
  }

  @Test
  @DisplayName("글 수정 실패 - 잘못된 비밀번호")
  void 글수정_실패_잘못된비밀번호() {
    ArticleUpdateServiceRequest request = ArticleUpdateServiceRequest.builder()
                                                                     .email("test@test.com")
                                                                     .password("wrongPassword")
                                                                     .title("updatedTitle")
                                                                     .content("updatedContent")
                                                                     .build();

    User user = User.builder()
                    .email("test@test.com")
                    .password("encodedPassword")
                    .username("testUser")
                    .build();

    doThrow(new BusinessException(ErrorCode.WRONG_PASSWORD))
        .when(userValidator).validateUserAndPassword("test@test.com", "wrongPassword");

    assertThatThrownBy(() -> articleService.updateArticle(1L, request))
        .isInstanceOf(BusinessException.class)
        .hasMessageContaining(ErrorCode.WRONG_PASSWORD.getMessage());

    verify(articleRepository, never()).save(any(Article.class));
  }

  @Test
  @DisplayName("글 수정 성공")
  void 글수정_성공() {
    ArticleUpdateServiceRequest request = ArticleUpdateServiceRequest.builder()
                                                                     .email("test@test.com")
                                                                     .password("test-pw")
                                                                     .title("updatedTitle")
                                                                     .content("updatedContent")
                                                                     .build();

    User user = User.builder()
                    .email("test@test.com")
                    .password("encodedPassword")
                    .username("testUser")
                    .build();

    Article article = Article.builder()
                             .user(user)
                             .title("oldTitle")
                             .content("oldContent")
                             .build();

    when(userValidator.validateUserAndPassword("test@test.com", "test-pw")).thenReturn(user);
    when(articleRepository.findById(anyLong())).thenReturn(Optional.of(article));

    ArticleUpdateResponse response = articleService.updateArticle(1L, request);

    assertThat(response.getTitle()).isEqualTo("updatedTitle");
    assertThat(response.getContent()).isEqualTo("updatedContent");
  }

  @Test
  @DisplayName("글 삭제 실패 - 존재하지 않는 사용자")
  void 글삭제_실패_존재하지않는사용자() {
    ArticleDeleteServiceRequest request = ArticleDeleteServiceRequest.builder()
                                                                     .email("test@test.com")
                                                                     .password("test-pw")
                                                                     .build();

    doThrow(new BusinessException(ErrorCode.USER_NOT_FOUND))
        .when(userValidator).validateUserAndPassword("test@test.com", "test-pw");

    assertThatThrownBy(() -> articleService.deleteArticle(1L, request))
        .isInstanceOf(BusinessException.class)
        .hasMessageContaining(ErrorCode.USER_NOT_FOUND.getMessage());

    verify(articleRepository, never()).deleteById(anyLong());
  }

  @Test
  @DisplayName("글 삭제 실패 - 존재하지 않는 게시글")
  void 글삭제_실패_존재하지않는게시글() {
    ArticleDeleteServiceRequest request = ArticleDeleteServiceRequest.builder()
                                                                     .email("test@test.com")
                                                                     .password("test-pw")
                                                                     .build();

    User user = User.builder()
                    .email("test@test.com")
                    .password("encodedPassword")
                    .username("testUser")
                    .build();

    when(userValidator.validateUserAndPassword("test@test.com", "test-pw")).thenReturn(user);
    when(articleRepository.findById(anyLong())).thenReturn(Optional.empty());

    assertThatThrownBy(() -> articleService.deleteArticle(1L, request))
        .isInstanceOf(BusinessException.class)
        .hasMessageContaining(ErrorCode.ARTICLE_NOT_FOUND.getMessage());

    verify(articleRepository, never()).deleteById(anyLong());
  }

  @Test
  @DisplayName("글 삭제 실패 - 잘못된 사용자")
  void 글삭제_실패_잘못된사용자() {
    ArticleDeleteServiceRequest request = ArticleDeleteServiceRequest.builder()
                                                                     .email("wronguser@test.com")
                                                                     .password("test-pw")
                                                                     .build();

    User user = User.builder()
                    .email("test@test.com")
                    .password("encodedPassword")
                    .username("testUser")
                    .build();

    Article article = Article.builder()
                             .user(user)
                             .title("test article")
                             .content("test content")
                             .build();

    doThrow(new BusinessException(ErrorCode.USER_NOT_MATCH))
        .when(userValidator).validateUserAndPassword("wronguser@test.com", "test-pw");

    assertThatThrownBy(() -> articleService.deleteArticle(1L, request))
        .isInstanceOf(BusinessException.class)
        .hasMessageContaining(ErrorCode.USER_NOT_MATCH.getMessage());

    verify(articleRepository, never()).deleteById(anyLong());
  }

  @Test
  @DisplayName("글 삭제 성공")
  void 글삭제_성공() {
    ArticleDeleteServiceRequest request = ArticleDeleteServiceRequest.builder()
                                                                     .email("test@test.com")
                                                                     .password("test-pw")
                                                                     .build();

    User user = User.builder()
                    .email("test@test.com")
                    .password("encodedPassword")
                    .username("testUser")
                    .build();

    Article article = Article.builder()
                             .user(user)
                             .title("test article")
                             .content("test content")
                             .build();

    when(userValidator.validateUserAndPassword("test@test.com", "test-pw")).thenReturn(user);
    when(articleRepository.findById(anyLong())).thenReturn(Optional.of(article));

    articleService.deleteArticle(1L, request);

    verify(articleRepository).deleteById(article.getId());
  }
}
