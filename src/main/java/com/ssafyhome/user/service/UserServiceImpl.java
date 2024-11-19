package com.ssafyhome.user.service;

import com.ssafyhome.user.dao.UserMapper;
import com.ssafyhome.user.dto.*;
import com.ssafyhome.user.entity.UserEntity;
import com.ssafyhome.common.util.ConvertUtil;
import com.ssafyhome.common.util.SecretUtil;
import com.ssafyhome.user.exception.*;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.MailSendException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

  private final BCryptPasswordEncoder passwordEncoder;
  private final UserMapper userMapper;
  private final SecretUtil secretUtil;
  private final ConvertUtil convertUtil;
  private final JavaMailSender mailSender;

  public UserServiceImpl(
      BCryptPasswordEncoder passwordEncoder,
      UserMapper userMapper,
      SecretUtil secretUtil,
      ConvertUtil convertUtil,
      JavaMailSender mailSender
  ) {

    this.passwordEncoder = passwordEncoder;
    this.userMapper = userMapper;
    this.secretUtil = secretUtil;
    this.convertUtil = convertUtil;
    this.mailSender = mailSender;
  }


  @Override
  public void register(UserDto userDto) {

    if(!checkPassword(userDto)) {
      throw new InvalidPasswordException("invalid password");
    }

    UserEntity userEntity = convertUtil.convert(userDto, UserEntity.class);
    userEntity.setUserPw(passwordEncoder.encode(userEntity.getUserPw()));

    userMapper.insertUser(userEntity);
  }

  @Override
  public String findUserId(FindUserDto findUserDto) {

    String userId = userMapper.getIdByNameAndEmail(findUserDto);
    if (userId != null) {
      return userId;
    } else {
      throw new UserNotFoundException();
    }
  }

  @Override
  public void findPassword(FindUserDto findUserDto) {

    if (userMapper.isUserExist(findUserDto)) {
      sendEmail(findUserDto.getUserEmail());
    } else {
      throw new UserNotFoundException();
    }
  }

  @Override
  public void sendEmail(String email) {

    String secret = secretUtil.makeRandomString(15);
    MimeMessage mimeMessage = mailSender.createMimeMessage();

    try {
      ClassPathResource template = new ClassPathResource("mail/mailForm.html");
      ClassPathResource headerLogo = new ClassPathResource("mail/zipchack_header_logo.png");
      ClassPathResource footerLogo = new ClassPathResource("mail/zipchack_footer_logo.png");
      String content;
      try {
        content = Files.readString(Paths.get(template.getURI()));
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
      content = content.replace("${secretKey}", secret);

      MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
      mimeMessageHelper.setTo(email);
      mimeMessageHelper.setSubject("\uD83C\uDF89 [Zipchak] 거의 다 왔어요! 인증번호를 입력해주세요");
      mimeMessageHelper.setText(content, true);
      mimeMessageHelper.addInline("headerLogo", headerLogo);
      mimeMessageHelper.addInline("footerLogo", footerLogo);
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
    if(userEntity == null) {
      throw new UserNotFoundException();
    }
    return convertUtil.convert(userEntity, UserDto.class);
  }

  @Override
  public List<UserDto> getUserList(UserSearchDto userSearchDto) {

    List<UserEntity> userEntityList = userMapper.getUserList(userSearchDto);
    if(userEntityList.isEmpty()) {
      throw new UserNotFoundException();
    }
    return convertUtil.convert(userEntityList, UserDto.class);
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
  public void checkIdDuplicate(String id) {

    if(userMapper.getUserById(id) != null) {
      throw new RuntimeException();
    };
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
  public void updateUser(long userSeq, UserDto userDto) {

    UserEntity userEntity = convertUtil.convert(userDto, UserEntity.class);
    userEntity.setUserSeq(userSeq);

    userMapper.updateUser(userEntity);
  }

  @Override
  public void deleteUser(long userSeq) {

    userMapper.deleteUser(userSeq);
  }

  private boolean checkPassword(UserDto userDto) {

    return userDto.getUserPw().equals(userDto.getUserPasswordConfirm());
  }

  private boolean checkPassword(PasswordDto passwordDto) {

    return passwordDto.getNewPassword().equals(passwordDto.getNewPasswordConfirm());
  }
}
