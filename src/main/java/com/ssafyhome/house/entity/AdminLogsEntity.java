package com.ssafyhome.house.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AdminLogsEntity {

	private int taskSeq;
	private String taskName;
	private String taskData;
	private LocalDateTime taskTime;
	private int adminSeq;
	private String ipv4;
	private String ipv6;
}
