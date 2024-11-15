package com.ssafyhome.user.controller;

import com.ssafyhome.user.dto.*;
import com.ssafyhome.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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
			description = "UserDto를 받아 회원을 등록합니다."
	)
	@ApiResponses({
		@ApiResponse(responseCode = "201", description="회원가입 완료"),
		@ApiResponse(responseCode = "400", description="비밀번호 비일치"),
		@ApiResponse(responseCode = "500", description="형식에 맞지 않는 데이터 \n\n"
				+ "1. 형식에 맞지 않는 전화번호"
				+ "2. 중복 아이디")
	  })
	@PostMapping("")
	public ResponseEntity<?> addUser(
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
			description = "String 객체의 email 받아 인증번호를 전송합니다."
	)
	  @ApiResponses({
		  @ApiResponse(responseCode = "200", description="이메일 전송 완료")
	  })

	@PostMapping("/send/mail")
	public ResponseEntity<?> sendEmail(
			@RequestBody
			@Schema(
			        description = "이메일 주소",
			        example = "x22z@naver.com"  // 예시 값 설정
			    )
			String email
	) {

		userService.sendEmail(email);
		return new ResponseEntity<>("send Email success", HttpStatus.OK);
	}

	@Operation(
			summary = "회원상세정보 제공",
			description = "String 객체의 userSeq 를 받아 UserDto 반환"
	)
	@GetMapping("")
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
			@ModelAttribute
			UserSearchDto userSearchDto
	) {

		return new ResponseEntity<>(userService.getUserList(userSearchDto), HttpStatus.OK);
	}

	@Operation(
			summary = "이메일 확인",
			description = "String 객체의 key 와 email 받아 이메일을 확인합니다."
	)
	  @ApiResponses({
		  @ApiResponse(responseCode = "500", description="유효하지 않은 이메일인 경우, Optional error")
	  })

	@GetMapping("/check/mail")
	public ResponseEntity<?> checkEmailSecret(
			@Parameter(
			          name = "email",
			          description = "secret key 를 전송한 mail"
			      )
			@RequestParam(value="email")
			String email,

			@Parameter(
			          name = "key",
			          description = "메일에 전송된 secret key"
			      )
			@RequestParam(value="key")
			String key
	) {

		return new ResponseEntity<>(userService.checkEmailSecret(new EmailSecretDto(email, key)), HttpStatus.OK);
	}

	@Operation(
			summary = "아이디 중복확인",
			description = "String 객체의 userId 받아 기존에 있는 id인지 확인합니다."
	)
	@GetMapping("/check/duplicate")
	public ResponseEntity<?> checkIdDuplicate(
			@RequestParam
			@Parameter(
			          name = "id",
			          description = "user id"
			      )

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
