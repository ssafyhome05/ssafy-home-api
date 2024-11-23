package com.ssafyhome.ai.service;

import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.stereotype.Service;

@Service
public class AiChatService {

	private final OpenAiChatModel openAiChatModel;

	public AiChatService(OpenAiChatModel openAiChatModel) {

		this.openAiChatModel = openAiChatModel;
	}

	public ChatResponse generateResponse(String promptText) {
		String command = "다음 질문에 대해서 설명해주세요: {message}";
		PromptTemplate promptTemplate = new PromptTemplate(command);
		promptTemplate.add("message", promptText);

		return openAiChatModel.call(promptTemplate.create());
	}
}
