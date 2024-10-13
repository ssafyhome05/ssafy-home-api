package com.ssafyhome.model.service.impl;

import com.ssafyhome.exception.DecryptUserSeqException;
import com.ssafyhome.exception.EncryptUserSeqException;
import com.ssafyhome.exception.InvalidEmailSecretException;
import com.ssafyhome.exception.InvalidPasswordException;
import com.ssafyhome.model.dao.mapper.UserMapper;
import com.ssafyhome.model.dto.*;
import com.ssafyhome.model.entity.mysql.UserEntity;
import com.ssafyhome.model.service.UserService;
import com.ssafyhome.util.SecretUtil;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.MailSendException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

  private final BCryptPasswordEncoder passwordEncoder;
  private final UserMapper userMapper;
  private final SecretUtil secretUtil;
  private final JavaMailSender mailSender;

  public UserServiceImpl(
      BCryptPasswordEncoder passwordEncoder,
      UserMapper userMapper,
      SecretUtil secretUtil,
      JavaMailSender mailSender
  ) {

    this.passwordEncoder = passwordEncoder;
    this.userMapper = userMapper;
    this.secretUtil = secretUtil;
    this.mailSender = mailSender;
  }


  @Override
  public void register(UserDto userDto) {

    if(!checkPassword(userDto)) {
      throw new InvalidPasswordException("invalid password");
    }

    UserEntity userEntity = new UserEntity();
    userEntity.setUserId(userDto.getUserId());
    userEntity.setUserPw(passwordEncoder.encode(userDto.getUserPassword()));
    userEntity.setUserEmail(userDto.getUserEmail());
    userEntity.setUserName(userDto.getUserName());

    userMapper.insertUser(userEntity);
  }

  @Override
  public String findUserId(FindUserDto findUserDto) {

    String userId = userMapper.getIdByNameAndEmail(findUserDto);
    if (userId != null) {
      return userId;
    } else {
      throw new NotFoundException("user not found");
    }
  }

  @Override
  public void findPassword(FindUserDto findUserDto) {

    if (userMapper.isUserExist(findUserDto)) {
      sendEmail(findUserDto.getUserEmail());
    } else {
      throw new NotFoundException("user not found");
    }
  }

  @Override
  public void sendEmail(String email) {

    String secret = secretUtil.makeRandomString(15);
    MimeMessage mimeMessage = mailSender.createMimeMessage();

    try {
      MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, false, "UTF-8");
      mimeMessageHelper.setTo(email);
      mimeMessageHelper.setSubject("SSAFY HOME 이메일 인증");
      mimeMessageHelper.setText(secret, true);
      mailSender.send(mimeMessage);
      secretUtil.addSecretOnRedis(email, secret);
    }
    catch (MessagingException e) {
      throw new MailSendException("send Email fail");
    }
  }

  @Override
  public UserDto getUserInfo(String userSeq) {

    UserEntity userEntity = userMapper.getUserBySeq(userSeq);
    return UserDto.builder()
        .userSeq(userEntity.getUserSeq())
        .userId(userEntity.getUserId())
        .userEmail(userEntity.getUserEmail())
        .build();
  }

  @Override
  public List<UserDto> getUserList(UserSearchDto userSearchDto) {

    List<UserEntity> userEntityList = userMapper.getUserList(userSearchDto);
    return userEntityList.stream()
        .map(userEntity -> UserDto.builder()
            .userSeq(userEntity.getUserSeq())
            .userId(userEntity.getUserId())
            .userEmail(userEntity.getUserEmail())
            .build())
        .toList();
  }

  @Override
  public String checkEmailSecret(EmailSecretDto emailSecretDto) {

    if (secretUtil.checkSecret(emailSecretDto.getEmail(), emailSecretDto.getSecret())) {
      secretUtil.removeSecretOnRedis(emailSecretDto.getEmail());
      long userSeq = userMapper.getSeqByEmail(emailSecretDto.getEmail());
      try {
        return secretUtil.encrypt(String.valueOf(userSeq));
      } catch (Exception e) {
        throw new EncryptUserSeqException("encrypt fail");
      }
    }
    else {
      throw new InvalidEmailSecretException("invalid email secret");
    }
  }

  @Override
  public boolean checkIdDuplicate(String id) {

    return userMapper.getUserById(id) == null;
  }

  @Override
  public void changePassword(String userSeq, PasswordDto passwordDto) {

    if (userSeq == null) {
      userSeq = SecurityContextHolder.getContext().getAuthentication().getName();
      if(!userMapper.checkPassword(
          userSeq,
          passwordEncoder.encode(passwordDto.getOldPassword()))
      ) {
        throw new InvalidPasswordException("invalid password");
      }
    } else {
      try {
        userSeq = secretUtil.decrypt(userSeq);
      } catch (Exception e) {
        throw new DecryptUserSeqException("decrypt fail");
      }
    }

    if (checkPassword(passwordDto)) {
      userMapper.patchPassword(userSeq, passwordEncoder.encode(passwordDto.getNewPassword()));
    } else {
      throw new InvalidPasswordException("invalid password");
    }
  }

  @Override
  public void updateUser(UserDto userDto) {

    UserEntity userEntity = new UserEntity();
    userEntity.setUserId(userDto.getUserId());
    userEntity.setUserEmail(userDto.getUserEmail());
    userEntity.setUserName(userDto.getUserName());
    userMapper.updateUser(userEntity);
  }

  @Override
  public void deleteUser(String userSeq) {

    userMapper.deleteUser(userSeq);
  }

  private boolean checkPassword(UserDto userDto) {

    return userDto.getUserPassword().equals(userDto.getUserPasswordConfirm());
  }

  private boolean checkPassword(PasswordDto passwordDto) {

    return passwordDto.getNewPassword().equals(passwordDto.getNewPasswordConfirm());
  }
}
