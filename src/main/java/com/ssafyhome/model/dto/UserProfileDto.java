package com.ssafyhome.model.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserProfileDto {

	private long userSeq;
	private String userId;
	private String userName;
}
