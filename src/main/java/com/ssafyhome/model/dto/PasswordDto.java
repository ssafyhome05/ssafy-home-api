package com.ssafyhome.model.dto;

import lombok.Data;

@Data
public class PasswordDto {

  private String userSeq;
  private String oldPassword;
  private String newPassword;
  private String newPasswordConfirm;
}
