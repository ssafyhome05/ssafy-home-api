package com.ssafyhome.house.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.concurrent.atomic.AtomicInteger;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PopulationEntity {

	private String admCd;                  // 행정구역 코드 (Primary Key)
	private String dongCode;               // 행정구역 코드 (Primary Key)
	private String cityName;               // 행정구역 코드 (Primary Key)
  private String totPpltn;               // 총 인구
  private String ppltnDnsty;             // 인구 밀도
  private String agedChildIdx;           // 노령화 지수
  private String corpCnt;                // 사업체 수
  private String totHouse;               // 총 주택 수
  private int ageUnder20Population;      // 20대 미만
  private int age2030Population;      // 40대 미만
  private int age4060Population;      // 70대 미만
  private int ageOver70Population;      // 70대 이상
}
