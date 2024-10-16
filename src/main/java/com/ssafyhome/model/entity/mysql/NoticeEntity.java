package com.ssafyhome.model.entity.mysql;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class NoticeEntity {

	private long noticeSeq;
	private String noticeTitle;
	private String noticeContent;
	private LocalDateTime createdAt;
	private LocalDateTime modifiedAt;
}
