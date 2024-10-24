package com.ssafyhome.model.dto.house;

import lombok.Data;

import java.time.Duration;

@Data
public class HouseInfoTask {

	private Duration duration;
	private int totalRows;
	private String TaskName;
}
