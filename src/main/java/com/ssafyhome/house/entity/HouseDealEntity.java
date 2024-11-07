package com.ssafyhome.house.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HouseDealEntity {

	private String dealSeq;
	private String aptSeq;
	private String aptDong;
	private String floor;
	private int dealYear;
	private int dealMonth;
	private int dealDay;
	private double excluUseAr;
	private String dealAmount;
}
