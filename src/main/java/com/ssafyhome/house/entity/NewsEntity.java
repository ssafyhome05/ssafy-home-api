package com.ssafyhome.house.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NewsEntity {

	  private String id;                //
	  private String dongCode;          //법정동코드
	  private String title;
	  private String cityName;          //
	  private String ref;
	  private String img;               // 이미지링크
	  private String createAt;          // 

}


