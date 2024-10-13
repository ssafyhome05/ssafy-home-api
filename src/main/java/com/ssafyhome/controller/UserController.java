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
			summary = "",
			description = ""
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
			summary = "",
			description = ""
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
			summary = "",
			description = ""
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
			summary = "",
			description = ""
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
			summary = "",
			description = ""
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
			summary = "",
			description = ""
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
			summary = "",
			description = ""
	)
	@GetMapping("/check/duplicate")
	public ResponseEntity<?> checkIdDuplicate(
			@RequestParam
			String userId
	) {

		return new ResponseEntity<>(userService.checkIdDuplicate(userId), HttpStatus.OK);
	}

	@Operation(
			summary = "",
			description = ""
	)
	@PatchMapping("/{userSeq}")
	public ResponseEntity<?> changePassword(
			@PathVariable(required = false)
			String userSeq,

			@RequestBody
			PasswordDto passwordDto
	) {

		userService.changePassword(userSeq, passwordDto);
		return new ResponseEntity<>("change password success", HttpStatus.OK);
	}

	@Operation(
			summary = "",
			description = ""
	)
	@PutMapping("/{userSeq}")
	@PreAuthorize("hasRole('ROLE_ADMIN') or  #userSeq == authentication.name")
	public ResponseEntity<?> updateUserInfo(
			@PathVariable
			String userSeq,

			@RequestBody
			UserDto userDto
	) {

		userService.updateUser(userDto);
		return new ResponseEntity<>("update user success", HttpStatus.OK);
	}

	@Operation(
			summary = "",
			description = ""
	)
	@DeleteMapping("/{userSeq}")
	@PreAuthorize("hasRole('ROLE_ADMIN') or  #userSeq == authentication.name")
	public ResponseEntity<?> deleteUser(
			@PathVariable
			String userSeq
	) {

		userService.deleteUser(userSeq);
		return new ResponseEntity<>("delete user success", HttpStatus.OK);
	}
}
