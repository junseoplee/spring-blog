package project.springblog.exception;

import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException e, HttpServletRequest request) {
    BindingResult bindingResult = e.getBindingResult();
    String firstErrorMessage = bindingResult.getFieldErrors().getFirst().getDefaultMessage();

    List<String> errors = bindingResult.getFieldErrors().stream()
                                       .map(DefaultMessageSourceResolvable::getDefaultMessage)
                                       .collect(Collectors.toList());
    log.info("MethodArgumentNotValidException 발생: {}", errors);

    ErrorResponse errorResponse = ErrorResponse.of(HttpStatus.BAD_REQUEST, firstErrorMessage, request.getRequestURI());
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
  }

  @ExceptionHandler(BusinessException.class)
  public ResponseEntity<ErrorResponse> handleBusinessException(BusinessException e, HttpServletRequest request) {
    log.info("BusinessException: {}", e.getMessage());

    ErrorResponse errorResponse = ErrorResponse.of(e.getStatus(), e.getMessage(), request.getRequestURI());
    return ResponseEntity.status(e.getStatus()).body(errorResponse);
  }
}
