package com.ssafyhome.user.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserEntity {

  private long userSeq;
  private String userId;
  private String userPw;
  private String userEmail;
  private String userName;
  private String userPhone;
  private String userZipcode;
  private String userAddress;
  private String userAddress2;
  private String socialType;
  private String socialPlatform;
  private LocalDateTime createdAt;
}
