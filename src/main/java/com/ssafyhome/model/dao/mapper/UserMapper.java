package com.ssafyhome.model.dao.mapper;

import com.ssafyhome.model.dto.FindUserDto;
import com.ssafyhome.model.dto.UserSearchDto;
import com.ssafyhome.model.entity.mysql.UserEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface UserMapper {

  UserEntity getUserBySeqAndEmail(@Param("userSeq") String seq, @Param("userEmail") String email);
  UserEntity getUserById(String id);
  void insertUser(UserEntity user);
  void updateUser(UserEntity user);
  void deleteUser(String userSeq);
  String getIdByNameAndEmail(FindUserDto findUserDto);
  boolean isUserExist(FindUserDto findUserDto);
  boolean checkPassword(@Param("userSeq") String seq, @Param("password") String password);
  void patchPassword(@Param("userSeq") String userSeq, @Param("password") String newPassword);
  List<UserEntity> getUserList(UserSearchDto userSearchDto);
  UserEntity getUserBySeq(String userSeq);
  long getSeqByEmail(String email);
}
