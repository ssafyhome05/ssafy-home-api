package com.ssafyhome.ai.controller;

import com.ssafyhome.ai.service.AiChatService;
import com.ssafyhome.common.response.ResponseMessage;
import com.ssafyhome.house.code.HouseResoponseCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AiController {

	private final AiChatService aiChatService;

	public AiController(AiChatService aiChatService) {
		this.aiChatService = aiChatService;
	}

	@GetMapping("/test")
	public ResponseEntity<ResponseMessage.CustomMessage> test(String question) {

		return ResponseMessage.responseDataEntity(
				HouseResoponseCode.OK,
				aiChatService.generateResponse(question)
		);
	}
}
