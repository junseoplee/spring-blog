package project.springblog.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

  DUPLICATE_MAIL(HttpStatus.BAD_REQUEST, "[Error] 중복된 이메일입니다.");

  private final HttpStatus status;
  private final String message;
}

