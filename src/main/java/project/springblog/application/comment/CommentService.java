package project.springblog.application.comment;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.springblog.application.comment.request.CommentCreateServiceRequest;
import project.springblog.application.comment.request.CommentDeleteServiceRequest;
import project.springblog.application.comment.request.CommentUpdateServiceRequest;
import project.springblog.application.comment.response.CommentCreateResponse;
import project.springblog.application.comment.response.CommentUpdateResponse;
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
    Article existArticle = findArticleById(articleId);

    Comment comment = Comment.builder()
                             .content(request.getContent())
                             .user(existUser)
                             .article(existArticle)
                             .build();

    Comment savedComment = commentRepository.save(comment);
    return new CommentCreateResponse(savedComment.getId(), existUser.getEmail(), savedComment.getContent());
  }

  @Transactional
  public CommentUpdateResponse updateComment(Long articleId, Long commentId, CommentUpdateServiceRequest request) {
    User existUser = userValidator.validateUserAndPassword(request.getEmail(), request.getPassword());
    Article existArticle = findArticleById(articleId);
    Comment foundComment = findCommentById(commentId);

    validateOwnership(foundComment, existUser, existArticle);

    foundComment.update(request.getContent());
    return new CommentUpdateResponse(foundComment.getId(), existUser.getEmail(), foundComment.getContent());
  }

  @Transactional
  public void deleteComment(Long articleId, Long commentId, CommentDeleteServiceRequest request) {
    User existUser = userValidator.validateUserAndPassword(request.getEmail(), request.getPassword());
    Article existArticle = findArticleById(articleId);
    Comment foundComment = findCommentById(commentId);

    validateOwnership(foundComment, existUser, existArticle);

    commentRepository.deleteById(foundComment.getId());
  }

  private Article findArticleById(Long articleId) {
    return articleRepository.findById(articleId)
                            .orElseThrow(() -> new BusinessException(ErrorCode.ARTICLE_NOT_FOUND));
  }

  private Comment findCommentById(Long commentId) {
    return commentRepository.findById(commentId)
                            .orElseThrow(() -> new BusinessException(ErrorCode.COMMENT_NOT_FOUND));
  }

  private void validateOwnership(Comment comment, User user, Article article) {
    if (!comment.getUser().getEmail().equals(user.getEmail())) {
      throw new BusinessException(ErrorCode.USER_NOT_MATCH);
    }
    if (!comment.getArticle().getId().equals(article.getId())) {
      throw new BusinessException(ErrorCode.ARTICLE_NOT_MATCH);
    }
  }
}
