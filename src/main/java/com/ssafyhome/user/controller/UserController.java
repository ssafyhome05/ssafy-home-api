package com.ssafyhome.user.controller;

import com.ssafyhome.common.response.ResponseMessage;
import com.ssafyhome.user.dto.*;
import com.ssafyhome.user.response.UserResponseCode;
import com.ssafyhome.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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
	  @ApiResponses({
		  @ApiResponse(responseCode = "200", description="id 반환"),
		  @ApiResponse(responseCode = "201", description="email key 전송"),
	  })

	@PostMapping("/find/{type}")
	public ResponseEntity<ResponseMessage.CustomMessage> findUserInfo(
    			@Parameter(
			          name = "type",
			          description = "#### user의 id 혹은 password 를 찾습니다. \n\n"
			          		+ "1. type 에 'id'를 입력하는 경우 \n\n"
			          		+ "    userName 과 userEmail 로 id 를 반환합니다.\n\n"
			          		+ "2. 'password' 를 입력하는 경우 \n\n"
			          		+ "    userEmail 로 key를 보냅니다. ",
			          example = "id"
			      )
			@PathVariable("type")
			String type,

			@RequestBody
		    @Schema(
		        description = "findUserDto",
		        example = "{\r\n"
		        		+ "  \"userName\": \"하성민\",\r\n"
		        		+ "  \"userEmail\": \"x22z@naver.com\",\r\n"
		        		+ "  \"userId\": \"\"\r\n"
		        		+ "}"  // 예시 값 설정
		    )
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
			description = "String 객체의 email 받아 인증번호를 전송합니다."
	)
	  @ApiResponses({
		  @ApiResponse(responseCode = "200", description="이메일 전송 완료")
	  })

	@PostMapping("/send/mail")
	public ResponseEntity<ResponseMessage.CustomMessage> sendEmail(
			@RequestBody
			@Schema(
			        description = "이메일 주소",
			        example = "x22z@naver.com"  // 예시 값 설정
			    )
			String email
	) {

		userService.sendEmail(email);
		return ResponseMessage.responseBasicEntity(UserResponseCode.MAIL_SEND_SUCCESS);
	}

	@Operation(
			summary = "회원상세정보 제공",
			description = "userSeq 에 해당하는 UserDto 를 반환합니다."
	)
	@GetMapping("")
	@PreAuthorize("hasRole('ROLE_ADMIN') or  #userSeq == authentication.name")
	public ResponseEntity<ResponseMessage.CustomMessage> getUserInfo(
			@RequestParam("userSeq")

			String userSeq
	) {

		return ResponseMessage.responseDataEntity(UserResponseCode.OK, userService.getUserInfo(userSeq));
	}

	@GetMapping("/info")
	public ResponseEntity<ResponseMessage.CustomMessage> getUserInfo(){

		boolean isUser = SecurityContextHolder.getContext().getAuthentication().getAuthorities().iterator().next().equals("ROLE_USER");
		if(isUser) {
			UserDto userDto = userService.getUserInfo(SecurityContextHolder.getContext().getAuthentication().getName());
			return ResponseMessage.responseDataEntity(UserResponseCode.OK, userDto);
		}
		return ResponseMessage.responseDataEntity(UserResponseCode.OK, new AdminDto("3","ADMIN"));
	}

	@Operation(
			summary = "사용자 목록 조회 (관리자전용)",
			description = "UserSearchDto 로 페이징작업을 마친 유저 정보를 반환합니다."
	)
	@ApiResponses({
		  @ApiResponse(responseCode = "200", description="사용자 목록 반환")
	  })
	@GetMapping("/list/{page}")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public ResponseEntity<ResponseMessage.CustomMessage> getUserList(

			@PathVariable
			int page,

			@RequestParam
			int size
	) {

		return ResponseMessage.responseDataEntity(UserResponseCode.OK, userService.getUserList(page, size));
	}

	@Operation(
			summary = "이메일 확인",
			description = "String 객체의 key 와 email 받아 이메일을 확인합니다."
	)
	  @ApiResponses({
		  @ApiResponse(responseCode = "500", description="유효하지 않은 이메일인 경우, Optional error")
	  })

	@GetMapping("/check/mail")
	public ResponseEntity<ResponseMessage.CustomMessage> checkEmailSecret(
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

		return ResponseMessage.responseDataEntity(UserResponseCode.VERIFICATION_SUCCESS, userService.checkEmailSecret(new EmailSecretDto(email, key)));
	}

	@Operation(
			summary = "아이디 중복확인",
			description = "String 객체의 userId 받아 기존에 있는 id인지 확인합니다."
	)
	  @ApiResponses({
		  @ApiResponse(responseCode = "200", description="### 정상작동 \n\n"
		  		+ "1. false : 가능하지 않은 아이디 입니다. (중복아이디) \n\n"
		  		+ "2. true  : 가능한 아이디 입니다. ")
	  })

	@GetMapping("/check/duplicate")
	public ResponseEntity<ResponseMessage.CustomMessage> checkIdDuplicate(
    @RequestParam(value="userId")
			@Parameter(
			          name = "userId",
			          description = "user id"
			      )
			String userId
	) {

		userService.checkIdDuplicate(userId);
		return ResponseMessage.responseBasicEntity(UserResponseCode.NOT_DUPLICATE);
	}

	@Operation(
			summary = "비밀번호 변경",
			description = " passwordDto 받아 비밀번호를 변경합니다."
	)
	@PatchMapping("")
	public ResponseEntity<ResponseMessage.CustomMessage> changePassword(
			@RequestBody
			@Schema(
			        description = "passwordDto",
			        example = "{\r\n"
			        		+ "  \"userSeq\": \"13\",\r\n"
			        		+ "  \"oldPassword\": \"xman119\",\r\n"
			        		+ "  \"newPassword\": \"xman1199\",\r\n"
			        		+ "  \"newPasswordConfirm\": \"xman1199\"\r\n"
			        		+ "}"  // 예시 값 설정
			    )
			PasswordDto passwordDto
	) {
		String userSeq = passwordDto.getUserSeq();
		userService.changePassword(userSeq, passwordDto);
		return ResponseMessage.responseBasicEntity(UserResponseCode.PASSWORD_CHANGED);
	}

	@Operation(
			summary = "회원정보 수정 (관리자 or 본인계정)",
			description = "userSeq 와 UserDto 로 회원정보를 수정합니다."
	)
	@PutMapping("/{userSeq}")
	@PreAuthorize("hasRole('ROLE_ADMIN') or  #userSeq == authentication.name")
	public ResponseEntity<ResponseMessage.CustomMessage> updateUserInfo(
			@PathVariable("userSeq")
			@Parameter(
			          name = "userSeq",
			          description = "user 의 userSeq",
			          example = "13"
			      )
      long userSeq,

			@RequestBody
			@Schema(
			        description = "passwordDto",
			        example = "{\r\n"
			        		+ "  \"userSeq\": 13,\r\n"
			        		+ "  \"userId\": \"xman227\",\r\n"
			        		+ "  \"userPw\": \"xman119\",\r\n"
			        		+ "  \"userPasswordConfirm\": \"string\",\r\n"
			        		+ "  \"userName\": \"하성민\",\r\n"
			        		+ "  \"userEmail\": \"x22z@naver.com\",\r\n"
			        		+ "  \"userPhone\": \"string\",\r\n"
			        		+ "  \"userZipcode\": \"string\",\r\n"
			        		+ "  \"userAddress\": \"string\",\r\n"
			        		+ "  \"userAddress2\": \"string\",\r\n"
			        		+ "  \"socialType\": true,\r\n"
			        		+ "  \"socialPlatform\": \"string\"\r\n"
			        		+ "}"  // 예시 값 설정
			    )
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
    
			@Parameter(
			          name = "userSeq",
			          description = "사용자번호",
			          example="1"
			      )
			@PathVariable("userSeq")
			long userSeq
	) {

		userService.deleteUser(userSeq);
		return ResponseMessage.responseBasicEntity(UserResponseCode.USER_DELETED);
	}
}
