package com.ssafyhome.user.controller;

import com.ssafyhome.common.response.ResponseMessage;
import com.ssafyhome.user.dto.*;
import com.ssafyhome.user.response.UserResponseCode;
import com.ssafyhome.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@Tag(
		name = "User Controller",
		description = "회원가입 및 로그인 등 회원관리"
)
@RestController
@RequestMapping("/user")
public class UserController {

	private final UserService userService;

	public UserController(UserService userService) {

		this.userService = userService;
	}

	@Operation(
			summary = "회원가입",
			description = "UserDto를 받아 회원 등록"
	)
	@PostMapping("")
	public ResponseEntity<ResponseMessage.CustomMessage> addUser(
			@RequestBody
			UserDto userDto
	) {

		userService.register(userDto);
		return ResponseMessage.responseBasicEntity(UserResponseCode.USER_CREATED);
	}

	@Operation(
			summary = "회원정보 찾기",
			description = "FindUserDto 를 받아 userId 반환"
	)
	@PostMapping("/find/{type}")
	public ResponseEntity<ResponseMessage.CustomMessage> findUserInfo(
			@PathVariable
			String type,

			@RequestBody
			FindUserDto findUserDto
	) {

		if (type.equals("id")) {
			String id = userService.findUserId(findUserDto);
			return ResponseMessage.responseDataEntity(UserResponseCode.FIND_ID, id);
		}
		else {
			userService.findPassword(findUserDto);
			return ResponseMessage.responseBasicEntity(UserResponseCode.MAIL_SEND_SUCCESS);
		}
	}

	@Operation(
			summary = "이메일 전송",
			description = "String 객체의 email 받아 이메일 전송"
	)
	@PostMapping("/send/mail")
	public ResponseEntity<ResponseMessage.CustomMessage> sendEmail(
			@RequestBody
			String email
	) {

		userService.sendEmail(email);
		return ResponseMessage.responseBasicEntity(UserResponseCode.MAIL_SEND_SUCCESS);
	}

	@Operation(
			summary = "회원상세정보 제공",
			description = "String 객체의 userSeq 를 받아 UserDto 반환"
	)
	@GetMapping("")
	@PreAuthorize("hasRole('ROLE_ADMIN') or  #userSeq == authentication.name")
	public ResponseEntity<ResponseMessage.CustomMessage> getUserInfo(
			@RequestParam
			String userSeq
	) {

		return ResponseMessage.responseDataEntity(UserResponseCode.OK, userService.getUserInfo(userSeq));
	}

	@GetMapping("/info")
	public ResponseEntity<ResponseMessage.CustomMessage> getUserInfo(){

		UserDto userDto = userService.getUserInfo(SecurityContextHolder.getContext().getAuthentication().getName());
		return ResponseMessage.responseDataEntity(UserResponseCode.OK, userDto);
	}

	@Operation(
			summary = "사용자 목록 조회",
			description = "UserSearchDto 받아 List<UserDto> 반환"
	)
	@GetMapping("/list")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public ResponseEntity<ResponseMessage.CustomMessage> getUserList(
			@ModelAttribute
			UserSearchDto userSearchDto
	) {

		return ResponseMessage.responseDataEntity(UserResponseCode.OK, userService.getUserList(userSearchDto));
	}

	@Operation(
			summary = "이메일 확인",
			description = "String 객체의 key 와 email 받아 이메일 확인"
	)
	@GetMapping("/check/mail")
	public ResponseEntity<ResponseMessage.CustomMessage> checkEmailSecret(
			@RequestParam
			String email,

			@RequestParam
			String key
	) {

		return ResponseMessage.responseDataEntity(UserResponseCode.VERIFICATION_SUCCESS, userService.checkEmailSecret(new EmailSecretDto(email, key)));
	}

	@Operation(
			summary = "아이디 중복확인",
			description = "String 객체의 userId 받아 중복확인"
	)
	@GetMapping("/check/duplicate")
	public ResponseEntity<ResponseMessage.CustomMessage> checkIdDuplicate(
			@RequestParam
			String userId
	) {

		userService.checkIdDuplicate(userId);
		return ResponseMessage.responseBasicEntity(UserResponseCode.NOT_DUPLICATE);
	}

	@Operation(
			summary = "비밀번호 변경",
			description = "String 객체의 userSeq 와 passwordDto 받아 비밀번호 변경"
	)
	@PatchMapping("")
	public ResponseEntity<ResponseMessage.CustomMessage> changePassword(
			@RequestBody
			PasswordDto passwordDto
	) {
		String userSeq = passwordDto.getUserSeq();
		userService.changePassword(userSeq, passwordDto);
		return ResponseMessage.responseBasicEntity(UserResponseCode.PASSWORD_CHANGED);
	}

	@Operation(
			summary = "회원정보 수정",
			description = "String 객체의 userSeq 와 UserDto 받아 회원정보 수정"
	)
	@PutMapping("/{userSeq}")
	@PreAuthorize("hasRole('ROLE_ADMIN') or  #userSeq == authentication.name")
	public ResponseEntity<ResponseMessage.CustomMessage> updateUserInfo(
			@PathVariable
			long userSeq,

			@RequestBody
			UserDto userDto
	) {

		userService.updateUser(userSeq, userDto);
		return ResponseMessage.responseBasicEntity(UserResponseCode.USER_UPDATED);
	}

	@Operation(
			summary = "회원정보 삭제",
			description = "String 객체의 userSeq 받아서 해당하는 회원 삭제"
	)
	@DeleteMapping("/{userSeq}")
	@PreAuthorize("hasRole('ROLE_ADMIN') or  #userSeq == authentication.name")
	public ResponseEntity<ResponseMessage.CustomMessage> deleteUser(
			@PathVariable
			long userSeq
	) {

		userService.deleteUser(userSeq);
		return ResponseMessage.responseBasicEntity(UserResponseCode.USER_DELETED);
	}
}
