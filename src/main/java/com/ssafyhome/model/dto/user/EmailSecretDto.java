package com.ssafyhome.model.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class EmailSecretDto {

	private String email;
	private String secret;
}
