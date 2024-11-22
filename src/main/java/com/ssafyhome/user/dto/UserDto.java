package com.ssafyhome.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {

  private long userSeq;
  private String userId;
  private String userPw;
  private String userPasswordConfirm;
  private String userName;
  private String userEmail;
  private String userPhone;
  private String userZipcode;
  private String userAddress;
  private String userAddress2;
  private boolean socialType;
  private String socialPlatform;
  private LocalDateTime createdAt;
}
