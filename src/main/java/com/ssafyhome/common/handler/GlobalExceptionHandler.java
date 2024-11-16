package com.ssafyhome.common.handler;

import com.ssafyhome.auth.exception.AccessTokenExpiredException;
import com.ssafyhome.auth.exception.ExpiredRefreshException;
import com.ssafyhome.auth.exception.InvalidJwtException;
import com.ssafyhome.auth.response.AuthResponseCode;
import com.ssafyhome.common.response.ResponseMessage;
import com.ssafyhome.user.exception.*;
import com.ssafyhome.user.response.UserResponseCode;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailSendException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(InvalidPasswordException.class)
  public ResponseEntity<ResponseMessage.CustomMessage> handleInvalidPasswordException(InvalidPasswordException e) {
    return ResponseMessage.responseBasicEntity(UserResponseCode.INVALID_PASSWORD);
  }

  @ExceptionHandler(InvalidEmailSecretException.class)
  public ResponseEntity<ResponseMessage.CustomMessage> handleInvalidEmailSecretException(InvalidEmailSecretException e) {
    return ResponseMessage.responseBasicEntity(UserResponseCode.INVALID_VERIFICATION_CODE);
  }

  @ExceptionHandler(MailSendException.class)
  public ResponseEntity<ResponseMessage.CustomMessage> handleMailSendException() {
    return ResponseMessage.responseBasicEntity(UserResponseCode.MAIL_SEND_FAILED);
  }

  @ExceptionHandler(EncryptUserSeqException.class)
  public ResponseEntity<ResponseMessage.CustomMessage> handleEncryptUserSeqException(EncryptUserSeqException e) {
    return ResponseMessage.responseBasicEntity(UserResponseCode.ENCRYPTION_FAILED);
  }

  @ExceptionHandler(DecryptUserSeqException.class)
  public ResponseEntity<ResponseMessage.CustomMessage> handleDecryptUserSeqException(DecryptUserSeqException e) {
    return ResponseMessage.responseBasicEntity(UserResponseCode.DECRYPTION_FAILED);
  }

  @ExceptionHandler(InvalidJwtException.class)
  public ResponseEntity<ResponseMessage.CustomMessage> handleInvalidJwtException(InvalidJwtException e) {
    return ResponseMessage.responseBasicEntity(AuthResponseCode.INVALID_JWT_TOKEN);
  }

  @ExceptionHandler(AccessTokenExpiredException.class)
  public ResponseEntity<ResponseMessage.CustomMessage> handleAccessTokenExpiredException(AccessTokenExpiredException e) {
    return ResponseMessage.responseBasicEntity(AuthResponseCode.ACCESS_TOKEN_EXPIRED);
  }

  @ExceptionHandler(ExpiredRefreshException.class)
  public ResponseEntity<ResponseMessage.CustomMessage> handleExpiredJwtException(ExpiredRefreshException e) {
    return ResponseMessage.responseBasicEntity(AuthResponseCode.REFRESH_TOKEN_EXPIRED);
  }

  @ExceptionHandler(UserNotFoundException.class)
  public ResponseEntity<ResponseMessage.CustomMessage> handleUserNotFoundException(UserNotFoundException e) {
    return ResponseMessage.responseBasicEntity(UserResponseCode.USER_NOT_FOUND);
  }
}