package com.ssafyhome.model.service.impl;

import com.ssafyhome.api.gonggong.GonggongClient;
import com.ssafyhome.api.sgis.SGISClient;
import com.ssafyhome.api.sgis.SGISUtil;
import com.ssafyhome.model.dao.mapper.HouseMapper;
import com.ssafyhome.model.dto.api.GonggongAptTradeResponse;
import com.ssafyhome.model.dto.api.SgisGeoCode;
import com.ssafyhome.model.entity.mysql.DongCodeEntity;
import com.ssafyhome.model.entity.mysql.HouseDealEntity;
import com.ssafyhome.model.entity.mysql.HouseInfoEntity;
import com.ssafyhome.model.service.HouseService;
import com.ssafyhome.util.ConvertUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class HouseServiceImpl implements HouseService {

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
	@Transactional
	public void insertHouseData(int lawdCd, int dealYmd) {

		int totalRows, repeat = 1, seq = 1;
		do {

			GonggongAptTradeResponse response =
					gonggongClient.getRTMSDataSvcAptTradeDev(lawdCd, dealYmd, gonggongApiKey, repeat, 100);
			totalRows = response.getBody().getTotalCount();

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

				try{
					houseMapper.insertHouseInfo(houseInfoEntity);
				} catch (DuplicateKeyException e) {
					System.out.println(houseInfoEntity.getHouseSeq() + " is already exist");
				}
				try{
					houseMapper.insertHouseDeal(houseDealEntity);
				} catch (DuplicateKeyException e) {
					System.out.println(houseDealEntity.getDealSeq() + " is already exist");
				}
			}

		} while (repeat++ * 100 < totalRows);
	}
}
