package com.ssafyhome.user.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class UserListDto {

  private int total;
  private List<UserDto> content;
}
