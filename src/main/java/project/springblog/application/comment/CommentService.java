package project.springblog.application.comment;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.springblog.application.comment.request.CommentCreateServiceRequest;
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

  @Transactional
  public CommentUpdateResponse updateComment(Long articleId, Long commentId, CommentUpdateServiceRequest request) {
    User existUser = userValidator.validateUserAndPassword(request.getEmail(), request.getPassword());

    Article existArticle = articleRepository.findById(articleId)
                                            .orElseThrow(() -> new BusinessException(ErrorCode.ARTICLE_NOT_FOUND));

    Comment foundComment = commentRepository.findById(commentId)
                                           .orElseThrow(() -> new BusinessException(ErrorCode.COMMENT_NOT_FOUND));

    if (!foundComment.getUser().getEmail().equals(request.getEmail())) {
      throw new BusinessException(ErrorCode.USER_NOT_MATCH);
    }

    if (!foundComment.getArticle().getId().equals(existArticle.getId())) {
      throw new BusinessException(ErrorCode.ARTICLE_NOT_MATCH);
    }

    foundComment.update(request.getContent());

    return new CommentUpdateResponse(foundComment.getId(), existUser.getEmail(), foundComment.getContent());
  }
}
