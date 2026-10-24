package ru.tbank.smart.budget.api.handler;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import ru.tbank.smart.budget.exception.BadRequestException;
import ru.tbank.smart.budget.exception.EntityAlreadyExistsException;
import ru.tbank.smart.budget.exception.EntityNotFoundException;
import ru.tbank.smart.budget.exception.ForbiddenException;
import ru.tbank.smart.budget.model.ErrorCodeDto;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

  private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

  @ExceptionHandler(EntityAlreadyExistsException.class)
  public ResponseEntity<ErrorCodeDto> handleEntityAlreadyExists(EntityAlreadyExistsException ex) {
    ErrorCodeDto error = new ErrorCodeDto(ex.getMessage());
    return ResponseEntity.status(CONFLICT).body(error);
  }

  @ExceptionHandler(EntityNotFoundException.class)
  public ResponseEntity<ErrorCodeDto> handleUserNotFound(EntityNotFoundException ex) {
    ErrorCodeDto error = new ErrorCodeDto(ex.getMessage());
    return ResponseEntity.status(NOT_FOUND).body(error);
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ErrorCodeDto> handleAllExceptions(Exception ex) {
    log.error("Внутренняя ошибка сервера: ", ex);
    ErrorCodeDto error = new ErrorCodeDto("Внутренняя ошибка сервера");
    return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(error);
  }

  @ExceptionHandler(ForbiddenException.class)
  public ResponseEntity<ErrorCodeDto> handleForbiddenException(ForbiddenException ex) {
    log.error("Ошибка доступа: ", ex);
    ErrorCodeDto error = new ErrorCodeDto(ex.getMessage());
    return ResponseEntity.status(FORBIDDEN).body(error);
  }

  @ExceptionHandler(BadRequestException.class)
  public ResponseEntity<ErrorCodeDto> handleBadRequestException(BadRequestException ex) {
    ErrorCodeDto error = new ErrorCodeDto(ex.getMessage());
    return ResponseEntity.status(BAD_REQUEST).body(error);
  }
}
