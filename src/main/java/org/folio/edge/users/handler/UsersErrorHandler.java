package org.folio.edge.users.handler;

import jakarta.validation.ConstraintViolationException;
import lombok.extern.log4j.Log4j2;
import org.folio.users.domain.dto.Error;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpStatusCodeException;

@Log4j2
@RestControllerAdvice
public class UsersErrorHandler {

  private static final String INVALID_PIN_ERROR_MESSAGE = "PIN is invalid";
  private static final String PATRON_PIN_VERIFY_URI = "patron-pin/verify";

  @ExceptionHandler(HttpStatusCodeException.class)
  public ResponseEntity<Error> handleRestClientResponseException(HttpStatusCodeException exception) {
    var properErrorMessage = exception.getResponseBodyAsString();
    var status = exception.getStatusCode().value();
    var errorResponse = buildErrorResponse(status, properErrorMessage, exception);
    return ResponseEntity.status(exception.getStatusCode())
        .body(errorResponse);
  }

  @ExceptionHandler({ConstraintViolationException.class})
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public Error handleExceptionWithBadRequestStatus(RuntimeException exception) {
    return buildErrorResponse(HttpStatus.BAD_REQUEST.value(), exception.getMessage(), exception);
  }

  private Error buildErrorResponse(int status, String message, Exception exception) {
    log.error("Not valid request cause, {}", message);
    log.debug(message, exception);
    var errorResponse = new Error();
    errorResponse.setCode(status);
    errorResponse.setErrorMessage(message);
    return errorResponse;
  }

}
