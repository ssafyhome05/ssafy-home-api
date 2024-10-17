package com.ssafyhome.model.entity.mysql;

import lombok.Data;

@Data
public class HouseDealEntity {

	private long dealSeq;
	private String aptSeq;
	private String aptDong;
	private String floor;
	private int dealYear;
	private int dealMonth;
	private int dealDay;
	private double exclusiveUseAr;
	private String dealAmount;
}
