package project.springblog.exception;

import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ErrorResponse {

  private LocalDateTime time;

  private String status;

  private String message;

  private String requestURI;

  @Builder
  private ErrorResponse(LocalDateTime time, String status, String message, String requestURI) {
    this.time = time;
    this.status = status;
    this.message = message;
    this.requestURI = requestURI;
  }

  public static ErrorResponse of(HttpStatus status, String message, String requestURI) {
    return ErrorResponse.builder()
                        .time(LocalDateTime.now())
                        .status(status.name())
                        .message(message)
                        .requestURI(requestURI)
                        .build();
  }
}
