package com.ssafyhome.house.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PopulationEntity {

	private String admCd;               // 행정구역 코드 (Primary Key)
    private String totPpltn;            // 총 인구
    private Double ppltnDnsty;          // 인구 밀도
    private String agedChildIdx;        // 노령화 지수
    private String corpCnt;             // 사업체 수
    private String totHouse;            // 총 주택 수
}
