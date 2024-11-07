package com.ssafyhome.api.gonggong;

import com.ssafyhome.api.gonggong.dto.GonggongAptTradeResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(
		name = "gonggongAPI",
		url = "https://apis.data.go.kr",
		configuration = FeignXmlDecoderConfig.class
)
public interface GonggongClient {

	@GetMapping("/1613000/RTMSDataSvcAptTradeDev/getRTMSDataSvcAptTradeDev")
	GonggongAptTradeResponse getRTMSDataSvcAptTradeDev(

			@RequestParam("LAWD_CD")
			int lawdCd,

			@RequestParam("DEAL_YMD")
			int dealYmd,

			@RequestParam("serviceKey")
			String serviceKey,

			@RequestParam("pageNo")
			int pageNo,

			@RequestParam("numOfRows")
			int numsOfRows
	);
}
