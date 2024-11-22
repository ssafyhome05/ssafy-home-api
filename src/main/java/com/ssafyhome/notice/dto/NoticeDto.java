package com.ssafyhome.notice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NoticeDto {

	private long noticeSeq;
	private String noticeTitle;
	private String noticeContent;
	private LocalDateTime createdAt;
	private LocalDateTime modifiedAt;
}
