package com.ssafyhome.common.api.sgis.dto;


import java.util.List;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class SgisPopulation {

	
	private String id;

	private List<Result> result;

	@JsonProperty("errMsg")
	private String errMsg;
	
	@JsonProperty("errCd")
	private String errCd;
	
	@JsonProperty("trId")
	private String trId;
	

	// 프로젝트에서는 3개의 지표 시각화
	// 1. 총인구 totPpltn
	// 2. 인구밀도 (소수점 두자리) ppltnDnsty
	// 3. 노령화지수, 백명당 노인 x명 agedChildIdx
	
	@Data
	public static class Result{
		

	    private String admCd;               // 행정구역코드
	    private String admNm;               // 행정구역명 (집계구에서는 나오지 않음)
	    private String totPpltn;            // 총인구							  
	    private double avgAge;              // 평균나이 (소수점 한자리)			
	    private double ppltnDnsty;          // 인구밀도 (소수점 두자리)
	    private String agedChildIdx;        // 노령화지수 (일백명당 명)
	    private String oldageSuprtPer;      // 노년부양비 (일백명당 명)
	    private String juvSuprtPer;         // 유년부양비 (일백명당 명)
	    private String totFamily;           // 총가구
	    private double avgFmemberCnt;       // 평균가구원수 (소수점 한자리)
	    private String totHouse;            // 총주택
	    private String nonggaCnt;           // 농가 (가구)
	    private String nonggaPpltn;         // 농가 인구
	    private String imgaCnt;             // 임가 (가구) (산림업종사 가구 - 벌목, 버섯채취 등)
	    private String imgaPpltn;           // 임가 인구  
	    private String naesuogaCnt;         // 내수면 어가 (가구) (민물 어업종사 가구)
	    private String naesuogaPpltn;       // 내수면 어가 인구
	    private String haesuogaCnt;         // 해수면 어가 (가구) (바닷물 어업종사 가구)
	    private String haesuogaPpltn;       // 해수면 어가 인구
	    private String employeeCnt;         // 종업원수 (전체 사업체)
	    private String corpCnt;             // 사업체수 (전체 사업체)

	}
	
}
