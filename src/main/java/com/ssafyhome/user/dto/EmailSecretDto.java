package com.ssafyhome.user.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class EmailSecretDto {

	private String email;
	private String secret;
}
