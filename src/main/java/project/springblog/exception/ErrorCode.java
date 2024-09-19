package project.springblog.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

  DUPLICATE_MAIL(HttpStatus.BAD_REQUEST, "[Error] 중복된 이메일입니다."),
  USER_NOT_FOUND(HttpStatus.NOT_FOUND, "[Error] 사용자를 찾을 수 없습니다."),
  WRONG_PASSWORD(HttpStatus.BAD_REQUEST, "잘못된 비밀번호입니다."),
  ARTICLE_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 게시글을 찾을 수 없습니다."),
  USER_NOT_MATCH(HttpStatus.BAD_REQUEST, "사용자가 일치하지 않습니다.");

  private final HttpStatus status;
  private final String message;
}

