package com.ssafyhome;

import com.ssafyhome.common.api.gonggong.GonggongClient;
import com.ssafyhome.common.api.sgis.SGISClient;
import com.ssafyhome.common.api.sgis.SGISUtil;
import com.ssafyhome.common.api.gonggong.dto.GonggongAptTradeResponse;
import com.ssafyhome.common.api.sgis.dto.SgisGeoCode;
import com.ssafyhome.house.entity.HouseDealEntity;
import com.ssafyhome.house.entity.HouseInfoEntity;
import com.ssafyhome.common.util.ConvertUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class HouseServiceTest {

	@Value("${gonggong.API-KEY.decode}")
	private String gonggongApiKey;

	private final GonggongClient gonggongClient;
	private final ConvertUtil convertUtil;
	private final SGISClient sgisClient;
	private final SGISUtil sgisUtil;

	@Autowired
	public HouseServiceTest(
			GonggongClient gonggongClient,
			ConvertUtil convertUtil,
			SGISClient sgisClient,
			SGISUtil sgisUtil
	) {

		this.gonggongClient = gonggongClient;
		this.convertUtil = convertUtil;
		this.sgisClient = sgisClient;
		this.sgisUtil = sgisUtil;
	}

	@Test
	public void convertTest() {

		GonggongAptTradeResponse response =
				gonggongClient.getRTMSDataSvcAptTradeDev(11110, 202101, gonggongApiKey, 1, 1);

		for(GonggongAptTradeResponse.Item item : response.getBody().getItems()) {

			HouseInfoEntity houseInfoEntity = convertUtil.convert(item, HouseInfoEntity.class);
			houseInfoEntity.setHouseSeq(item.getAptSeq());

			SgisGeoCode.Result.ResultData geoCode =
					sgisClient.getGeocode(sgisUtil.getAccessToken(), item.getRoadNm() + " " + item.getRoadNmBonbun())
							.getResult().getResultdata().get(0);
			houseInfoEntity.setLatitude(geoCode.getY());
			houseInfoEntity.setLongitude(geoCode.getX());

			HouseDealEntity houseDealEntity = convertUtil.convert(item, HouseDealEntity.class);

			System.out.println(houseInfoEntity);
			System.out.println(houseDealEntity);
		}
	}
}
