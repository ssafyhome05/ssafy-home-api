package com.ssafyhome.common.handler;

import com.ssafyhome.common.exception.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailSendException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.webjars.NotFoundException;

@ControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(InvalidPasswordException.class)
  public ResponseEntity<String> handleInvalidPasswordException(InvalidPasswordException e) {
    return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(InvalidEmailSecretException.class)
  public ResponseEntity<String> handleInvalidEmailSecretException(InvalidEmailSecretException e) {
    return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(NotFoundException.class)
  public ResponseEntity<String> handleNotFoundException(NotFoundException e) {
    return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler(MailSendException.class)
  public ResponseEntity<String> handleMailSendException(MailSendException e) {
    return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
  }

  @ExceptionHandler(EncryptUserSeqException.class)
  public ResponseEntity<String> handleEncryptUserSeqException(EncryptUserSeqException e) {
    return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
  }

  @ExceptionHandler(DecryptUserSeqException.class)
  public ResponseEntity<String> handleDecryptUserSeqException(DecryptUserSeqException e) {
    return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
  }

  @ExceptionHandler(InvalidJwtException.class)
  public ResponseEntity<String> handleInvalidJwtException(InvalidJwtException e) {
    return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
  }

  @ExceptionHandler(AccessTokenExpiredException.class)
  public ResponseEntity<String> handleAccessTokenExpiredException(AccessTokenExpiredException e) {
    return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<String> handleException(Exception e) {
    return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
  }
}
