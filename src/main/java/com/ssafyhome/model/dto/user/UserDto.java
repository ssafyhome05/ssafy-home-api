package com.ssafyhome.model.dto.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Data;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {

  private long userSeq;
  private String userId;
  private String userPassword;
  private String userPasswordConfirm;
  private String userName;
  private String userEmail;
  private String userPhone;
  private String userZipcode;
  private String userAddress;
  private String userAddress2;
  private boolean socialType;
  private String socialPlatform;
}
