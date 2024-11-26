package com.ssafyhome.ai.controller;

import com.ssafyhome.ai.dto.PromptDealDto;
import com.ssafyhome.ai.dto.PromptResourceDto;
import com.ssafyhome.ai.service.AiChatService;
import com.ssafyhome.common.response.ResponseMessage;
import com.ssafyhome.house.code.HouseResoponseCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/ai")
public class AiController {

	private final AiChatService aiChatService;

	public AiController(AiChatService aiChatService) {
		this.aiChatService = aiChatService;
	}

	@PostMapping("/house")
	@PreAuthorize("hasRole('ROLE_USER')")
	public ResponseEntity<ResponseMessage.CustomMessage> recommendHouse(
			@RequestBody
			PromptResourceDto promptResourceDto
	) {
		System.out.println(promptResourceDto.getNativePrompt());
		return ResponseMessage.responseDataEntity(
				HouseResoponseCode.OK,
				aiChatService.generateResponse(promptResourceDto)
		);
	}
	
	@PostMapping("/deal")
	@PreAuthorize("hasRole('ROLE_USER')")
	public ResponseEntity<ResponseMessage.CustomMessage> predictHouse(
			@RequestBody
			PromptDealDto promptDealDto
	) {
		System.out.println(promptDealDto.getNativePrompt());
		System.out.println("deal 처리 댐니다 =======================================");
		return ResponseMessage.responseDataEntity(
				HouseResoponseCode.OK,
				aiChatService.generateDealResponse(promptDealDto)
		);
	}

	@GetMapping("/test")
	public ResponseEntity<ResponseMessage.CustomMessage> test(String prompt) {

		return ResponseMessage.responseDataEntity(
				HouseResoponseCode.OK,
				aiChatService.generateResponse(prompt)
		);
	}
}
