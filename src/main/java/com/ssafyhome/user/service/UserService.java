package com.ssafyhome.user.service;

import com.ssafyhome.user.dto.*;

import java.util.List;

public interface UserService {

  void register(UserDto userDto);
  String findUserId(FindUserDto findUserDto);
  void findPassword(FindUserDto findUserDto);
  void sendEmail(String email);
  UserDto getUserInfo(String userSeq);
  List<UserDto> getUserList(UserSearchDto userSearchDto);
  String checkEmailSecret(EmailSecretDto emailSecretDto);
  void checkIdDuplicate(String id);
  void changePassword(String userSeq, PasswordDto passwordDto);
  void updateUser(long userSeq, UserDto userDto);
  void deleteUser(long userSeq);


}
