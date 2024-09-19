package project.springblog.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class BusinessException extends RuntimeException {

  private final HttpStatus status;

  public BusinessException(ErrorCode errorCode) {
    super(errorCode.getMessage());
    this.status = errorCode.getStatus();
  }
}
