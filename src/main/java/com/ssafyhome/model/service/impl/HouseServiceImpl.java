package com.ssafyhome.model.service.impl;

import com.ssafyhome.api.gonggong.GonggongClient;
import com.ssafyhome.api.sgis.SGISClient;
import com.ssafyhome.api.sgis.SGISUtil;
import com.ssafyhome.model.dao.mapper.HouseMapper;
import com.ssafyhome.model.dto.api.GonggongAptTradeResponse;
import com.ssafyhome.model.dto.api.SgisGeoCode;
import com.ssafyhome.model.dto.house.HouseInfoSseEmitter;
import com.ssafyhome.model.entity.mysql.DongCodeEntity;
import com.ssafyhome.model.entity.mysql.HouseDealEntity;
import com.ssafyhome.model.entity.mysql.HouseInfoEntity;
import com.ssafyhome.model.service.HouseService;
import com.ssafyhome.util.ConvertUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class HouseServiceImpl implements HouseService {

	private final Map<String, HouseInfoSseEmitter> sseEmitters = new ConcurrentHashMap<>();

	@Value("${gonggong.API-KEY.decode}")
	private String gonggongApiKey;

	private final GonggongClient gonggongClient;
	private final HouseMapper houseMapper;
	private final ConvertUtil convertUtil;
	private final SGISClient sgisClient;
	private final SGISUtil sgisUtil;

	public HouseServiceImpl(
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

	@Override
	public String startHouseInfoTask(int dealYmd) {

		String requestId = UUID.randomUUID().toString();


		SseEmitter sseEmitter = new SseEmitter(Long.MAX_VALUE);
		sseEmitters.put(requestId, new HouseInfoSseEmitter(sseEmitter));

		CompletableFuture.runAsync(() -> {
			try {
				List<Integer> lawdCdList = houseMapper.getLawdCdList();
				int seq = 1;
				for (int lawdCd : lawdCdList) {
					insertHouseData(lawdCd, dealYmd, requestId);
					sseEmitter.send(SseEmitter.event()
							.name(sseEmitters.get(requestId).getTaskName())
							.data("Task completed!! commit (" + seq++ + "/" + lawdCdList.size() + ")" )
					);
				}
			} catch (Exception e) {
				sseEmitter.completeWithError(e);
			} finally {
				sseEmitters.remove(requestId);
			}
		});

		return requestId;
	}

	@Override
	public SseEmitter getHouseInfoTask(String requestId) {

		if (!sseEmitters.containsKey(requestId)) {
			throw new NoSuchElementException();
		}
		return sseEmitters.get(requestId).getEmitter();
	}

	@Transactional
	protected void insertHouseData(int lawdCd, int dealYmd, String requestId) {

		int totalRows = 0 , repeat = 1, seq = 1;
		do {

			GonggongAptTradeResponse response =
					gonggongClient.getRTMSDataSvcAptTradeDev(lawdCd, dealYmd, gonggongApiKey, repeat, 100);
			if (totalRows == 0) {
				totalRows = response.getBody().getTotalCount();
				sseEmitters.get(requestId).setTaskName(lawdCd + "-" + dealYmd);
				sseEmitters.get(requestId).setTotalRows(totalRows);
				try {
					HouseInfoSseEmitter houseInfoTask = sseEmitters.get(requestId);
					houseInfoTask.getEmitter().send(
							SseEmitter.event()
									.name(houseInfoTask.getTaskName())
									.data("Total rows: " + houseInfoTask.getTotalRows())
					);
				} catch (Exception e) {}
			}

			for(GonggongAptTradeResponse.Item item : response.getBody().getItems()) {

				HouseInfoEntity houseInfoEntity = convertUtil.convert(item, HouseInfoEntity.class);
				houseInfoEntity.setHouseSeq(item.getAptSeq());

				DongCodeEntity dongCodeEntity = houseMapper.getSidoGugun(item.getSggCd() + item.getUmdCd());
				String dongName = dongCodeEntity.getSidoName() + " " + dongCodeEntity.getGugunName() + " " + dongCodeEntity.getDongName();

				SgisGeoCode.Result.ResultData geoCode =
						sgisClient.getGeocode(sgisUtil.getAccessToken(), dongName + " " + item.getJibun())
								.getResult().getResultdata().get(0);
				houseInfoEntity.setLatitude(geoCode.getY());
				houseInfoEntity.setLongitude(geoCode.getX());

				HouseDealEntity houseDealEntity = convertUtil.convert(item, HouseDealEntity.class);
				houseDealEntity.setDealSeq(lawdCd + "-" + dealYmd + "-" + seq++);

				String alert = "";
				try{
					houseMapper.insertHouseInfo(houseInfoEntity);
					alert += houseInfoEntity.getHouseSeq() + ": commit ready\n";
				} catch (DuplicateKeyException e) {
					alert += houseInfoEntity.getHouseSeq() + " is already exist\n";
				}
				try{
					houseMapper.insertHouseDeal(houseDealEntity);
					alert += houseDealEntity.getDealSeq() + ": commit ready";
				} catch (DuplicateKeyException e) {
					alert += houseDealEntity.getDealSeq() + " is already exist";
				}

				try {
					sseEmitters.get(requestId).getEmitter().send(SseEmitter.event().name(alert));
				} catch (Exception e) {}
			}

		} while (repeat++ * 100 < totalRows);
	}
}
