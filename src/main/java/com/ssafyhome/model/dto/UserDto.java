package com.ssafyhome.model.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
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
