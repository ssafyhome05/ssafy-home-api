package com.ssafyhome.ai.dto;

import lombok.Data;

import java.util.List;

@Data
public class PromptResourceDto {

	private String nativePrompt;
	private List<PromptVariable> promptVariables;

	@Data
	public static class PromptVariable {

		private String key;
		private List<String> values;
		private int priority;
	}
}
