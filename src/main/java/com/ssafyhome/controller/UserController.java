package com.ssafyhome.controller;

import com.ssafyhome.model.dto.*;
import com.ssafyhome.model.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
	@PostMapping("/")
	public ResponseEntity<?> registerUser(
			@RequestBody
			UserDto userDto
	) {

		userService.register(userDto);
		return new ResponseEntity<>(HttpStatus.CREATED);
	}

	@Operation(
			summary = "회원정보 찾기",
			description = "FindUserDto 를 받아 userId 반환"
	)
	@PostMapping("/find/{type}")
	public ResponseEntity<?> findUserInfo(
			@PathVariable
			String type,

			@RequestBody
			FindUserDto findUserDto
	) {

		if (type.equals("id")) {
			String id = userService.findUserId(findUserDto);
			return new ResponseEntity<>(id, HttpStatus.OK);
		}
		else {
			userService.findPassword(findUserDto);
			return new ResponseEntity<>("check email secret", HttpStatus.OK);
		}
	}

	@Operation(
			summary = "이메일 전송",
			description = "String 객체의 email 받아 이메일 전송"
	)
	@PostMapping("/send/mail")
	public ResponseEntity<?> sendEmail(
			@RequestBody
			String email
	) {

		userService.sendEmail(email);
		return new ResponseEntity<>("send Email success", HttpStatus.CREATED);
	}

	@Operation(
			summary = "회원상세정보 제공",
			description = "String 객체의 userSeq 를 받아 UserDto 반환"
	)
	@GetMapping("/")
	@PreAuthorize("hasRole('ROLE_ADMIN') or  #userSeq == authentication.name")
	public ResponseEntity<UserDto> getUserInfo(
			@RequestParam
			String userSeq
	) {

		return new ResponseEntity<>(userService.getUserInfo(userSeq), HttpStatus.OK);
	}

	@Operation(
			summary = "사용자 목록 조회",
			description = "UserSearchDto 받아 List<UserDto> 반환"
	)
	@GetMapping("/list")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public ResponseEntity<List<UserDto>> getUserList(
			@RequestBody
			UserSearchDto userSearchDto
	) {

		return new ResponseEntity<>(userService.getUserList(userSearchDto), HttpStatus.OK);
	}

	@Operation(
			summary = "이메일 확인",
			description = "String 객체의 key 와 email 받아 이메일 확인"
	)
	@GetMapping("/check/mail")
	public ResponseEntity<?> checkEmailSecret(
			@RequestParam
			String email,

			@RequestParam
			String key
	) {

		return new ResponseEntity<>(userService.checkEmailSecret(new EmailSecretDto(email, key)), HttpStatus.OK);
	}

	@Operation(
			summary = "아이디 중복확인",
			description = "String 객체의 userId 받아 중복확인"
	)
	@GetMapping("/check/duplicate")
	public ResponseEntity<?> checkIdDuplicate(
			@RequestParam
			String userId
	) {

		return new ResponseEntity<>(userService.checkIdDuplicate(userId), HttpStatus.OK);
	}

	@Operation(
			summary = "비밀번호 변경",
			description = "String 객체의 userSeq 와 passwordDto 받아 비밀번호 변경"
	)
	@PatchMapping("")
	public ResponseEntity<?> changePassword(
			@RequestBody
			PasswordDto passwordDto
	) {
		String userSeq = passwordDto.getUserSeq();
		userService.changePassword(userSeq, passwordDto);
		return new ResponseEntity<>("change password success", HttpStatus.OK);
	}

	@Operation(
			summary = "회원정보 수정",
			description = "String 객체의 userSeq 와 UserDto 받아 회원정보 수정"
	)
	@PutMapping("/{userSeq}")
	@PreAuthorize("hasRole('ROLE_ADMIN') or  #userSeq == authentication.name")
	public ResponseEntity<?> updateUserInfo(
			@PathVariable
			long userSeq,

			@RequestBody
			UserDto userDto
	) {

		userService.updateUser(userSeq, userDto);
		return new ResponseEntity<>("update user success", HttpStatus.OK);
	}

	@Operation(
			summary = "회원정보 삭제",
			description = "String 객체의 userSeq 받아서 해당하는 회원 삭제"
	)
	@DeleteMapping("/{userSeq}")
	@PreAuthorize("hasRole('ROLE_ADMIN') or  #userSeq == authentication.name")
	public ResponseEntity<?> deleteUser(
			@PathVariable
			long userSeq
	) {

		userService.deleteUser(userSeq);
		return new ResponseEntity<>("delete user success", HttpStatus.OK);
	}
}
