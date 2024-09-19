package project.springblog.application.article;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.springblog.application.article.request.ArticleCreateServiceRequest;
import project.springblog.application.article.response.ArticleCreateResponse;
import project.springblog.domain.article.Article;
import project.springblog.domain.article.repository.ArticleRepository;
import project.springblog.domain.user.User;
import project.springblog.domain.user.repository.UserRepository;
import project.springblog.exception.BusinessException;
import project.springblog.exception.ErrorCode;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ArticleService {

  private final ArticleRepository articleRepository;
  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;

  @Transactional
  public ArticleCreateResponse createArticle(ArticleCreateServiceRequest request) {
    User existUser = userRepository.findByEmail(request.getEmail())
                                   .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

    if (!passwordEncoder.matches(request.getPassword(), existUser.getPassword())) {
      throw new BusinessException(ErrorCode.WRONG_PASSWORD);
    }

    Article article = Article.builder()
                             .user(existUser)
                             .title(request.getTitle())
                             .content(request.getContent())
                             .build();

    Article savedArticle = articleRepository.save(article);

    return new ArticleCreateResponse(savedArticle.getId(), request.getEmail(), savedArticle.getTitle(), savedArticle.getContent());
  }
}
