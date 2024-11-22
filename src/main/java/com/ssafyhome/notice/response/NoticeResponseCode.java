package com.ssafyhome.notice.response;

import com.ssafyhome.common.response.ResponseCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum NoticeResponseCode implements ResponseCode {
  OK(200070, "공지사항 불러오기 성공", HttpStatus.OK);

  private final int code;
  private final String message;
  private final HttpStatus httpStatus;
}
