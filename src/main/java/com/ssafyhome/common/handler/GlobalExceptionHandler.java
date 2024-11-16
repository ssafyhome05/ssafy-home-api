package com.ssafyhome.common.handler;

import com.ssafyhome.auth.response.AuthResponseCode;
import com.ssafyhome.common.exception.*;
import com.ssafyhome.common.response.ResponseMessage;
import com.ssafyhome.user.response.UserResponseCode;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailSendException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(InvalidPasswordException.class)
  public ResponseEntity<ResponseMessage.CustomMessage> handleInvalidPasswordException(InvalidPasswordException e) {
    return ResponseMessage.builder()
        .responseCode(UserResponseCode.INVALID_PASSWORD)
        .build()
        .responseEntity();
  }

  @ExceptionHandler(InvalidEmailSecretException.class)
  public ResponseEntity<ResponseMessage.CustomMessage> handleInvalidEmailSecretException(InvalidEmailSecretException e) {
    return ResponseMessage.builder()
        .responseCode(UserResponseCode.INVALID_VERIFICATION_CODE)
        .build()
        .responseEntity();
  }

  @ExceptionHandler(MailSendException.class)
  public ResponseEntity<ResponseMessage.CustomMessage> handleMailSendException() {
    return ResponseMessage.builder()
        .responseCode(UserResponseCode.MAIL_SEND_FAILED)
        .build()
        .responseEntity();
  }

  @ExceptionHandler(EncryptUserSeqException.class)
  public ResponseEntity<ResponseMessage.CustomMessage> handleEncryptUserSeqException(EncryptUserSeqException e) {
    return ResponseMessage.builder()
        .responseCode(UserResponseCode.ENCRYPTION_FAILED)
        .build()
        .responseEntity();
  }

  @ExceptionHandler(DecryptUserSeqException.class)
  public ResponseEntity<ResponseMessage.CustomMessage> handleDecryptUserSeqException(DecryptUserSeqException e) {
    return ResponseMessage.builder()
        .responseCode(UserResponseCode.DECRYPTION_FAILED)
        .build()
        .responseEntity();
  }

  @ExceptionHandler(InvalidJwtException.class)
  public ResponseEntity<ResponseMessage.CustomMessage> handleInvalidJwtException(InvalidJwtException e) {
    return ResponseMessage.builder()
        .responseCode(AuthResponseCode.INVALID_JWT_TOKEN)
        .build()
        .responseEntity();
  }

  @ExceptionHandler(AccessTokenExpiredException.class)
  public ResponseEntity<ResponseMessage.CustomMessage> handleAccessTokenExpiredException(AccessTokenExpiredException e) {
    return ResponseMessage.builder()
        .responseCode(AuthResponseCode.ACCESS_TOKEN_EXPIRED)
        .build()
        .responseEntity();
  }

  @ExceptionHandler(ExpiredRefreshException.class)
  public ResponseEntity<ResponseMessage.CustomMessage> handleExpiredJwtException(ExpiredRefreshException e) {
    return ResponseMessage.builder()
        .responseCode(AuthResponseCode.REFRESH_TOKEN_EXPIRED)
        .build()
        .responseEntity();
  }
}