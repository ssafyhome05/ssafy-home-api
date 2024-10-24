package com.ssafyhome.model.service.impl;

import com.ssafyhome.api.gonggong.GonggongClient;
import com.ssafyhome.api.sgis.SGISClient;
import com.ssafyhome.api.sgis.SGISUtil;
import com.ssafyhome.exception.GonggongApplicationErrorException;
import com.ssafyhome.model.dao.mapper.HouseMapper;
import com.ssafyhome.model.dto.api.GonggongAptTradeResponse;
import com.ssafyhome.model.dto.api.SgisGeoCode;
import com.ssafyhome.model.dto.house.HouseInfoTask;
import com.ssafyhome.model.entity.mysql.DongCodeEntity;
import com.ssafyhome.model.entity.mysql.HouseDealEntity;
import com.ssafyhome.model.entity.mysql.HouseInfoEntity;
import com.ssafyhome.util.ConvertUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.time.Duration;
import java.time.LocalDateTime;

@Service
public class HouseInternalService {

	@Value("${gonggong.API-KEY.decode}")
	private String gonggongApiKey;

	private final GonggongClient gonggongClient;
	private final HouseMapper houseMapper;
	private final ConvertUtil convertUtil;
	private final SGISClient sgisClient;
	private final SGISUtil sgisUtil;

	public HouseInternalService(
			GonggongClient gonggongClient,
			HouseMapper houseMapper,
			ConvertUtil convertUtil,
			SGISClient sgisClient,
			SGISUtil sgisUtil
	) {

		this.gonggongClient = gonggongClient;
		this.houseMapper = houseMapper;
		this.convertUtil = convertUtil;
		this.sgisClient = sgisClient;
		this.sgisUtil = sgisUtil;
	}

	@Transactional
	protected HouseInfoTask insertHouseData(int lawdCd, int dealYmd, SseEmitter sseEmitter) throws Exception {

		HouseInfoTask houseInfoTask = new HouseInfoTask();
		LocalDateTime start = LocalDateTime.now();
		int totalRows = 0 , repeat = 1, seq = 1;
		do {

			GonggongAptTradeResponse response =
					gonggongClient.getRTMSDataSvcAptTradeDev(lawdCd, dealYmd, gonggongApiKey, repeat, 100);
			if (response.getCmmMsgHeader() != null) {
				GonggongAptTradeResponse.CmmMsgHeader header = response.getCmmMsgHeader();
				switch (header.getReturnReasonCode()) {
					case "01" -> throw new GonggongApplicationErrorException("Gonggong application error");
					default -> throw new Exception();
				}
			}
			if (totalRows == 0) {
				totalRows = response.getBody().getTotalCount();
				houseInfoTask.setTaskName(lawdCd + "-" + dealYmd);
				houseInfoTask.setTotalRows(totalRows);
			}

			for(GonggongAptTradeResponse.Item item : response.getBody().getItems()) {

				HouseDealEntity houseDealEntity = convertUtil.convert(item, HouseDealEntity.class);
				houseDealEntity.setDealSeq(lawdCd + "-" + dealYmd + "-" + seq++);

				try{
					houseMapper.insertHouseDeal(houseDealEntity);
				} catch (DuplicateKeyException e) {
				} catch (Exception ex) {

					HouseInfoEntity houseInfoEntity = convertUtil.convert(item, HouseInfoEntity.class);
					houseInfoEntity.setHouseSeq(item.getAptSeq());

					DongCodeEntity dongCodeEntity = houseMapper.getSidoGugun(item.getSggCd() + item.getUmdCd());
					String dongName;
					try {
						dongName = dongCodeEntity.getSidoName() + " " + dongCodeEntity.getGugunName() + " " + houseInfoEntity.getUmdNm();
					} catch (Exception e) {
						continue;
					}

					if(!houseMapper.isExistHouseInfo(houseInfoEntity.getHouseSeq())) {
						SgisGeoCode geoCode = sgisClient.getGeocode(sgisUtil.getAccessToken(), dongName + " " + item.getJibun());
						if(geoCode.getResult() != null){
							SgisGeoCode.Result.ResultData resultData =
									sgisClient.getGeocode(sgisUtil.getAccessToken(), dongName + " " + item.getJibun())
											.getResult().getResultdata().get(0);
							houseInfoEntity.setLatitude(resultData.getY());
							houseInfoEntity.setLongitude(resultData.getX());
						}
						else {
							try {
								sseEmitter.send(
										SseEmitter.event()
												.name("Not found jibun")
												.data(lawdCd + "-" + dealYmd + "-" + seq)
								);
							} catch (Exception e) {}
						}

						try{
							houseMapper.insertHouseInfo(houseInfoEntity);
						} catch (DuplicateKeyException e) {}
						try{
							houseMapper.insertHouseDeal(houseDealEntity);
						} catch (DuplicateKeyException e) {}
					}
				}
			}

		} while (repeat++ * 100 < totalRows);
		LocalDateTime end = LocalDateTime.now();
		houseInfoTask.setDuration(Duration.between(start, end));
		return houseInfoTask;
	}
}
