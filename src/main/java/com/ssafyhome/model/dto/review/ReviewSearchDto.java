package com.ssafyhome.model.dto.review;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ReviewSearchDto {

	private int page;
	private int size;
	private long userSeq;
	private String aptSeq;
	private LocalDateTime startAt;
	private LocalDateTime endAt;
	private String keyword;
}
