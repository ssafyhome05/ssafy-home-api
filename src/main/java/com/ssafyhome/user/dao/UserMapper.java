package com.ssafyhome.user.dao;

import com.ssafyhome.user.dto.FindUserDto;
import com.ssafyhome.user.dto.UserSearchDto;
import com.ssafyhome.user.entity.UserEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface UserMapper {

  UserEntity getUserBySeqAndEmail(@Param("userSeq") String seq, @Param("userEmail") String email);
  UserEntity getUserById(String id);
  void insertUser(UserEntity user);
  void insertOAuth2User(UserEntity user);
  void updateUser(UserEntity user);
  void deleteUser(long userSeq);
  String getIdByNameAndEmail(FindUserDto findUserDto);
  boolean isUserExist(FindUserDto findUserDto);
  boolean checkPassword(@Param("userSeq") String seq, @Param("password") String password);
  void patchPassword(@Param("userSeq") String userSeq, @Param("password") String newPassword);
  List<UserEntity> getUserList(UserSearchDto userSearchDto);
  UserEntity getUserBySeq(String userSeq);
  long getSeqByEmail(String email);
}
