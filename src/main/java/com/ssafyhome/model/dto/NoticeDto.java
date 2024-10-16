package com.ssafyhome.model.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class NoticeDto {

	private long noticeSeq;
	private String noticeTitle;
	private String noticeContent;
	private LocalDateTime createdAt;
	private LocalDateTime modifiedAt;
}
