package com.ssafyhome.house.service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicInteger;

import com.ssafyhome.house.dto.SseMessageDto;
import com.ssafyhome.house.code.SseMessageCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import com.ssafyhome.common.api.gonggong.GonggongClient;
import com.ssafyhome.common.api.gonggong.dto.GonggongAptTradeResponse;
import com.ssafyhome.common.api.sgis.SGISClient;
import com.ssafyhome.common.api.sgis.SGISUtil;
import com.ssafyhome.common.api.sgis.dto.SgisGeoCode;
import com.ssafyhome.common.entity.DongCodeEntity;
import com.ssafyhome.common.exception.GonggongApplicationErrorException;
import com.ssafyhome.common.util.ConvertUtil;
import com.ssafyhome.house.dao.HouseMapper;
import com.ssafyhome.house.entity.HouseDealEntity;
import com.ssafyhome.house.entity.HouseInfoEntity;

@Slf4j
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
	protected void insertHouseData(int lawdCd, int dealYmd, SseEmitter sseEmitter) throws Exception {

		int totalRows = 0, page = 1;
		AtomicInteger seq = new AtomicInteger(1);
		List<HouseDealEntity> dealEntityList = new ArrayList<>();
		ConcurrentHashMap<String, HouseInfoEntity> infoEntityMap = new ConcurrentHashMap<>();
		houseMapper.getExistAptSeq(String.valueOf(lawdCd)).forEach((aptSeq) -> infoEntityMap.put(aptSeq, new HouseInfoEntity()));

		do {
			GonggongAptTradeResponse response =
					gonggongClient.getRTMSDataSvcAptTradeDev(lawdCd, dealYmd, gonggongApiKey, page, 100);
			if (totalRows == 0) totalRows = response.getBody().getTotalCount();
			if (response.getCmmMsgHeader() != null) {
				GonggongAptTradeResponse.CmmMsgHeader header = response.getCmmMsgHeader();
				switch (header.getReturnReasonCode()) {
					case "01" -> throw new GonggongApplicationErrorException("Gonggong application error");
					case "04" -> throw new GonggongApplicationErrorException("Gonggong http error");
					default -> throw new Exception();
				}
			}

			CountDownLatch countDownLatch = new CountDownLatch(response.getBody().getItems().size());
			List<DongCodeEntity> dongCodeEntities = houseMapper.getSidoGugun(String.valueOf(lawdCd));
			for(GonggongAptTradeResponse.Item item : response.getBody().getItems()) {

				executorService.submit(() -> {
					try{
						HouseDealEntity houseDealEntity = convertUtil.convert(item, HouseDealEntity.class);
						houseDealEntity.setDealSeq(lawdCd + "-" + dealYmd + "-" + seq.getAndIncrement());
						dealEntityList.add(houseDealEntity);

						if(infoEntityMap.containsKey(item.getAptSeq())) return;

						HouseInfoEntity houseInfoEntity = convertUtil.convert(item, HouseInfoEntity.class);
						houseInfoEntity.setAptSeq(item.getAptSeq());

						DongCodeEntity dongCodeEntity = dongCodeEntities.stream().filter(entity -> entity.getBdongCode().equals(item.getSggCd() + item.getUmdCd())).findFirst().get();
						String dongName;
						try {
							dongName = dongCodeEntity.getSidoName() + " " + dongCodeEntity.getGugunName() + " " + houseInfoEntity.getUmdNm();
						} catch (Exception e) {
								throw new RuntimeException(e);
						}
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

								new SseMessageDto<>(
										SseMessageCode.NOT_FOUND_JIBUN,
										lawdCd + "-" + dealYmd + "-" + seq
								).sendEvent(sseEmitter);

							} catch (Exception e) {
							}
						}
						infoEntityMap.putIfAbsent(houseInfoEntity.getAptSeq(), houseInfoEntity);
					} catch (Exception e) {
						e.printStackTrace();
					}
					finally {
						countDownLatch.countDown();
					}
				});
			}

			try {
				countDownLatch.await();
			} catch (InterruptedException e) {}

		} while (page++ * 100 < totalRows);

		try {
			List<HouseInfoEntity> infoEntityList = infoEntityMap.values().stream().filter(entity -> entity.getAptSeq() != null).toList();
			if (!infoEntityList.isEmpty()) houseMapper.insertHouseInfo(infoEntityList);
			if (!dealEntityList.isEmpty()) houseMapper.insertHouseDeal(dealEntityList);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
