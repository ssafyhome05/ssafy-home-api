package com.ssafyhome.auth.entity;

import lombok.Data;

@Data
public class AdminEntity {

	private String adminSeq;
	private String adminPw;
	private String adminRole;

	public AdminEntity(String adminSeq, String adminRole) {

		this.adminSeq = adminSeq;
		this.adminRole = adminRole;
	}
}
