package com.ssafyhome.model.service;

import com.ssafyhome.model.dto.*;

import java.util.List;

public interface UserService {

  void register(UserDto userDto);
  String findUserId(FindUserDto findUserDto);
  void findPassword(FindUserDto findUserDto);
  void sendEmail(String email);
  UserDto getUserInfo(String userSeq);
  List<UserDto> getUserList(UserSearchDto userSearchDto);
  String checkEmailSecret(EmailSecretDto emailSecretDto);
  boolean checkIdDuplicate(String id);
  void changePassword(String userSeq, PasswordDto passwordDto);
  void updateUser(UserDto userDto);
  void deleteUser(String userSeq);


}
