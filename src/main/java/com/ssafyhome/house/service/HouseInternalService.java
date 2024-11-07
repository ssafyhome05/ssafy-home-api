package com.ssafyhome.house.service;

import com.ssafyhome.api.gonggong.GonggongClient;
import com.ssafyhome.api.sgis.SGISClient;
import com.ssafyhome.api.sgis.SGISUtil;
import com.ssafyhome.common.exception.GonggongApplicationErrorException;
import com.ssafyhome.house.dao.HouseMapper;
import com.ssafyhome.api.gonggong.dto.GonggongAptTradeResponse;
import com.ssafyhome.api.sgis.dto.SgisGeoCode;
import com.ssafyhome.house.dto.HouseInfoTask;
import com.ssafyhome.common.entity.DongCodeEntity;
import com.ssafyhome.house.entity.HouseDealEntity;
import com.ssafyhome.house.entity.HouseInfoEntity;
import com.ssafyhome.util.ConvertUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Semaphore;

@Service
public class HouseInternalService {

	@Value("${gonggong.API-KEY.decode}")
	private String gonggongApiKey;

	private final GonggongClient gonggongClient;
	private final HouseMapper houseMapper;
	private final ConvertUtil convertUtil;
	private final SGISClient sgisClient;
	private final SGISUtil sgisUtil;
	private final ExecutorService executorService;

	public HouseInternalService(
			GonggongClient gonggongClient,
			HouseMapper houseMapper,
			ConvertUtil convertUtil,
			SGISClient sgisClient,
			SGISUtil sgisUtil,
			ExecutorService executorService
	) {

		this.gonggongClient = gonggongClient;
		this.houseMapper = houseMapper;
		this.convertUtil = convertUtil;
		this.sgisClient = sgisClient;
		this.sgisUtil = sgisUtil;
		this.executorService = executorService;
	}

	@Transactional
	protected HouseInfoTask insertHouseData(int lawdCd, int dealYmd, SseEmitter sseEmitter, Semaphore semaphore) throws Exception {

		HouseInfoTask houseInfoTask = new HouseInfoTask();
		LocalDateTime start = LocalDateTime.now();
		int totalRows = 0 , repeat = 1, seq = 1;
		List<HouseInfoEntity> infoEntityList = new ArrayList<>();
		List<HouseDealEntity> dealEntityList = new ArrayList<>();
		Set<String> existAptSeq = houseMapper.getExistAptSeq(String.valueOf(lawdCd));

		do {
			GonggongAptTradeResponse response =
					gonggongClient.getRTMSDataSvcAptTradeDev(lawdCd, dealYmd, gonggongApiKey, repeat, 100);
			if (response.getCmmMsgHeader() != null) {
				GonggongAptTradeResponse.CmmMsgHeader header = response.getCmmMsgHeader();
				switch (header.getReturnReasonCode()) {
					case "01" -> throw new GonggongApplicationErrorException("Gonggong application error");
					case "04" -> throw new GonggongApplicationErrorException("Gonggong http error");
					default -> throw new Exception();
				}
			}
			if (totalRows == 0) {
				totalRows = response.getBody().getTotalCount();
				houseInfoTask.setTaskName(lawdCd + "-" + dealYmd);
				houseInfoTask.setTotalRows(totalRows);
			}

			CountDownLatch countDownLatch = new CountDownLatch(response.getBody().getItems().size());

			for(GonggongAptTradeResponse.Item item : response.getBody().getItems()) {

				try{
					HouseDealEntity houseDealEntity = convertUtil.convert(item, HouseDealEntity.class);
					houseDealEntity.setDealSeq(lawdCd + "-" + dealYmd + "-" + seq++);
					dealEntityList.add(houseDealEntity);

					if (existAptSeq.contains(houseDealEntity.getAptSeq())) continue;

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
						int finalSeq = seq;
						executorService.submit(() -> {
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
													.data(lawdCd + "-" + dealYmd + "-" + finalSeq)
									);
								} catch (Exception e) {
								}
							}
						});

						infoEntityList.add(houseInfoEntity);
						existAptSeq.add(houseInfoEntity.getHouseSeq());
					}
				} finally {
					countDownLatch.countDown();
				}
			}

			try {
				countDownLatch.await();
			} catch (InterruptedException e) {}

		} while (repeat++ * 100 < totalRows);

		semaphore.acquire();
		try {
			if (!infoEntityList.isEmpty()) houseMapper.insertHouseInfo(infoEntityList);
			if (!dealEntityList.isEmpty()) houseMapper.insertHouseDeal(dealEntityList);
		} catch (Exception e) {}
		semaphore.release();

		LocalDateTime end = LocalDateTime.now();
		houseInfoTask.setDuration(Duration.between(start, end));
		return houseInfoTask;
	}
}
