package org.folio.edge.users.handler;

import org.folio.users.domain.dto.Error;
import feign.FeignException;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Log4j2
@RestControllerAdvice
public class UsersErrorHandler {

  private static final String INVALID_PIN_ERROR_MESSAGE = "PIN is invalid";
  private static final String PATRON_PIN_VERIFY_URI = "patron-pin/verify";

  @ExceptionHandler(ConstraintViolationException.class)
  public ResponseEntity<Error> handleConstraintViolationException(ConstraintViolationException exception) {
    Error errorResponse = buildErrorResponse(HttpStatus.BAD_REQUEST.value(), exception.getMessage());
    return ResponseEntity.status(errorResponse.getCode())
        .body(errorResponse);
  }

  @ExceptionHandler(FeignException.class)
  public ResponseEntity<Error> handleFeignException(FeignException exception) {
    String properErrorMessage = exception.contentUTF8();
    if (HttpStatus.UNPROCESSABLE_ENTITY.value() == exception.status()
        && exception.request().url().contains(PATRON_PIN_VERIFY_URI)) {
      properErrorMessage = INVALID_PIN_ERROR_MESSAGE;
    }
    Error errorResponse = buildErrorResponse(exception.status(), properErrorMessage);

    return ResponseEntity.status(exception.status())
        .body(errorResponse);
  }

  private Error buildErrorResponse(int status, String message) {
    log.error("Error occurred during service chain call, {}", message);
    Error errorResponse = new Error();
    errorResponse.setCode(status);
    errorResponse.setErrorMessage(message);
    return errorResponse;
  }

}
