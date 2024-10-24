package com.ssafyhome.model.dto.notice;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class NoticeDto {

	private long noticeSeq;
	private String noticeTitle;
	private String noticeContent;
	private LocalDateTime createdAt;
}
