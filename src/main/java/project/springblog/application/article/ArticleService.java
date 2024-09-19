package project.springblog.application.article;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.springblog.application.article.request.ArticleCreateServiceRequest;
import project.springblog.application.article.request.ArticleDeleteServiceRequest;
import project.springblog.application.article.request.ArticleUpdateServiceRequest;
import project.springblog.application.article.response.ArticleCreateResponse;
import project.springblog.application.article.response.ArticleUpdateResponse;
import project.springblog.application.validator.UserValidator;
import project.springblog.domain.article.Article;
import project.springblog.domain.article.repository.ArticleRepository;
import project.springblog.domain.user.User;
import project.springblog.exception.BusinessException;
import project.springblog.exception.ErrorCode;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ArticleService {

  private final ArticleRepository articleRepository;
  private final UserValidator userValidator;

  @Transactional
  public ArticleCreateResponse createArticle(ArticleCreateServiceRequest request) {
    User existUser = userValidator.validateUserAndPassword(request.getEmail(), request.getPassword());

    Article article = Article.builder()
                             .user(existUser)
                             .title(request.getTitle())
                             .content(request.getContent())
                             .build();

    Article savedArticle = articleRepository.save(article);
    return new ArticleCreateResponse(savedArticle.getId(), request.getEmail(), savedArticle.getTitle(), savedArticle.getContent());
  }

  @Transactional
  public ArticleUpdateResponse updateArticle(Long articleId, ArticleUpdateServiceRequest request) {
    User existUser = userValidator.validateUserAndPassword(request.getEmail(), request.getPassword());
    Article foundArticle = findArticleById(articleId);

    validateOwnership(foundArticle, existUser);

    foundArticle.update(request.getTitle(), request.getContent());
    return new ArticleUpdateResponse(foundArticle.getId(), existUser.getEmail(), foundArticle.getTitle(), foundArticle.getContent());
  }

  @Transactional
  public void deleteArticle(Long articleId, ArticleDeleteServiceRequest request) {
    User existUser = userValidator.validateUserAndPassword(request.getEmail(), request.getPassword());
    Article foundArticle = findArticleById(articleId);

    validateOwnership(foundArticle, existUser);

    articleRepository.deleteById(foundArticle.getId());
  }

  private Article findArticleById(Long articleId) {
    return articleRepository.findById(articleId)
                            .orElseThrow(() -> new BusinessException(ErrorCode.ARTICLE_NOT_FOUND));
  }

  private void validateOwnership(Article article, User user) {
    if (!article.getUser().getEmail().equals(user.getEmail())) {
      throw new BusinessException(ErrorCode.USER_NOT_MATCH);
    }
  }
}
