package com.ssafyhome.model.dto.user;

import lombok.Data;

@Data
public class UserSearchDto {

	private int page;
	private int size;
	private String id;
	private String name;
	private String phone;
	private String email;
	private String socialPlatform;
}
