package com.ssafyhome.house.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AdminUpdatedInfoDto {

	private LocalDateTime taskTime;
	private String taskData;
	private int adminSeq;
	private String ipv4;
	private String ipv6;
}
