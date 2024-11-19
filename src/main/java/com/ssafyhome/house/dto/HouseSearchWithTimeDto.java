package com.ssafyhome.house.dto;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Data
public class HouseSearchWithTimeDto {

	private String dongCode;
//	@DateTimeFormat(pattern = "yyyy-MM-dd")
//	private LocalDate startDate;
//	@DateTimeFormat(pattern = "yyyy-MM-dd")
//	private LocalDate endDate;
	private String keyword;
	private long userSeq;
}
