package project.springblog.application.comment;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import project.springblog.application.validator.UserValidator;
import project.springblog.domain.article.Article;
import project.springblog.domain.article.repository.ArticleRepository;
import project.springblog.domain.comment.Comment;
import project.springblog.domain.comment.repository.CommentRepository;
import project.springblog.domain.user.User;
import project.springblog.exception.BusinessException;
import project.springblog.exception.ErrorCode;
import project.springblog.application.comment.request.CommentCreateServiceRequest;
import project.springblog.application.comment.response.CommentCreateResponse;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.*;

class CommentServiceTest {

  @Mock
  private CommentRepository commentRepository;

  @Mock
  private ArticleRepository articleRepository;

  @Mock
  private UserValidator userValidator;

  @InjectMocks
  private CommentService commentService;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  @DisplayName("댓글 작성 실패 - 존재하지 않는 사용자")
  void 댓글작성_실패_존재하지않는사용자() {
    CommentCreateServiceRequest request = CommentCreateServiceRequest.builder()
                                                                     .email("test@test.com")
                                                                     .password("wrongPassword")
                                                                     .content("test content")
                                                                     .build();

    doThrow(new BusinessException(ErrorCode.USER_NOT_FOUND))
        .when(userValidator).validateUserAndPassword("test@test.com", "wrongPassword");

    assertThatThrownBy(() -> commentService.createComment(1L, request))
        .isInstanceOf(BusinessException.class)
        .hasMessageContaining(ErrorCode.USER_NOT_FOUND.getMessage());

    verify(commentRepository, never()).save(any(Comment.class));
  }

  @Test
  @DisplayName("댓글 작성 실패 - 잘못된 비밀번호")
  void 댓글작성_실패_잘못된비밀번호() {
    CommentCreateServiceRequest request = CommentCreateServiceRequest.builder()
                                                                     .email("test@test.com")
                                                                     .password("wrongPassword")
                                                                     .content("test content")
                                                                     .build();

    User user = User.builder()
                    .email("test@test.com")
                    .password("encodedCorrectPassword")
                    .username("testUser")
                    .build();

    when(userValidator.validateUserAndPassword("test@test.com", "wrongPassword"))
        .thenThrow(new BusinessException(ErrorCode.WRONG_PASSWORD));

    assertThatThrownBy(() -> commentService.createComment(1L, request))
        .isInstanceOf(BusinessException.class)
        .hasMessageContaining(ErrorCode.WRONG_PASSWORD.getMessage());

    verify(commentRepository, never()).save(any(Comment.class));
  }

  @Test
  @DisplayName("댓글 작성 실패 - 존재하지 않는 게시글")
  void 댓글작성_실패_존재하지않는게시글() {
    CommentCreateServiceRequest request = CommentCreateServiceRequest.builder()
                                                                     .email("test@test.com")
                                                                     .password("test-pw")
                                                                     .content("test content")
                                                                     .build();

    User user = User.builder()
                    .email("test@test.com")
                    .password("encodedPassword")
                    .username("testUser")
                    .build();

    when(userValidator.validateUserAndPassword("test@test.com", "test-pw")).thenReturn(user);
    when(articleRepository.findById(anyLong())).thenReturn(Optional.empty());

    assertThatThrownBy(() -> commentService.createComment(1L, request))
        .isInstanceOf(BusinessException.class)
        .hasMessageContaining(ErrorCode.ARTICLE_NOT_FOUND.getMessage());

    verify(commentRepository, never()).save(any(Comment.class));
  }

  @Test
  @DisplayName("댓글 작성 성공")
  void 댓글작성_성공() {
    CommentCreateServiceRequest request = CommentCreateServiceRequest.builder()
                                                                     .email("test@test.com")
                                                                     .password("test-pw")
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

    Comment comment = Comment.builder()
                             .user(user)
                             .article(article)
                             .content("test content")
                             .build();

    when(userValidator.validateUserAndPassword("test@test.com", "test-pw")).thenReturn(user);
    when(articleRepository.findById(anyLong())).thenReturn(Optional.of(article));
    when(commentRepository.save(any(Comment.class))).thenReturn(comment);

    CommentCreateResponse response = commentService.createComment(1L, request);

    assertThat(response.getEmail()).isEqualTo("test@test.com");
    assertThat(response.getContent()).isEqualTo("test content");

    verify(commentRepository).save(any(Comment.class));
  }
}
