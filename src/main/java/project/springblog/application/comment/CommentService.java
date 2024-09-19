package project.springblog.application.comment;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.springblog.application.comment.request.CommentCreateServiceRequest;
import project.springblog.application.comment.response.CommentCreateResponse;
import project.springblog.application.validator.UserValidator;
import project.springblog.domain.article.Article;
import project.springblog.domain.article.repository.ArticleRepository;
import project.springblog.domain.comment.Comment;
import project.springblog.domain.comment.repository.CommentRepository;
import project.springblog.domain.user.User;
import project.springblog.exception.BusinessException;
import project.springblog.exception.ErrorCode;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CommentService {

  private final CommentRepository commentRepository;
  private final ArticleRepository articleRepository;
  private final UserValidator userValidator;

  @Transactional
  public CommentCreateResponse createComment(Long articleId, CommentCreateServiceRequest request) {
    User existUser = userValidator.validateUserAndPassword(request.getEmail(), request.getPassword());

    Article existArticle = articleRepository.findById(articleId)
                                            .orElseThrow(() -> new BusinessException(ErrorCode.ARTICLE_NOT_FOUND));

    Comment comment = Comment.builder()
                             .content(request.getContent())
                             .user(existUser)
                             .article(existArticle)
                             .build();

    Comment savedComment = commentRepository.save(comment);

    return new CommentCreateResponse(savedComment.getId(), existUser.getEmail(), savedComment.getContent());
  }
}
