package com.ssafyhome.model.dao.mapper;

import com.ssafyhome.model.dto.user.FindUserDto;
import com.ssafyhome.model.dto.user.UserSearchDto;
import com.ssafyhome.model.entity.mysql.UserEntity;
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
