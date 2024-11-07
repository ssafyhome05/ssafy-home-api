package com.ssafyhome.notice.entity;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class NoticeEntity {

	private long noticeSeq;
	private String noticeTitle;
	private String noticeContent;
	private LocalDateTime createdAt;
	private LocalDateTime modifiedAt;
}
