package com.ssafyhome.ai.util;

import com.ssafyhome.ai.dto.PromptResourceDto;
import com.ssafyhome.ai.dto.SpotStatEntity;
import com.ssafyhome.house.entity.HouseDealEntity;
import com.ssafyhome.house.entity.HouseInfoEntity;
import com.ssafyhome.house.entity.PopulationEntity;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class EntityToStringUtil {

	private String convert(HouseInfoEntity houseInfoEntity) {

		return "|아파트 매물 번호 -> " + houseInfoEntity.getAptSeq() + "|" +
				"지역 코드 -> " + houseInfoEntity.getSggCd() + "|" +
				"동 이름 -> " + houseInfoEntity.getUmdNm() + "|" +
				"아파트 이름 -> " + houseInfoEntity.getAptNm() + "|" +
				"건축년도 -> " + houseInfoEntity.getBuildYear() + "|" +
				"위도 -> " + houseInfoEntity.getLatitude() + "|" +
				"경도 -> " + houseInfoEntity.getLongitude() + "|\n";
	}

	private String convert(HouseDealEntity houseDealEntity) {

		return "|아파트 매물 번호 -> " + houseDealEntity.getAptSeq() + "|" +
				"거래 금액(만원) -> " + houseDealEntity.getDealAmount() + "|" +
				"거래 층 -> " + houseDealEntity.getFloor() + "|" +
				"면적(제곱미터) -> " + houseDealEntity.getExcluUseAr() + "|\n";
	}

	private String convert(PopulationEntity populationEntity) {

		return "|지역 코드 -> " + populationEntity.getDongCode() + "|" +
				"총 인구 -> " + populationEntity.getTotPpltn() + "|" +
				"인구밀도 -> " + populationEntity.getPpltnDnsty() + "|" +
				"20대 미만 -> " + populationEntity.getAgeUnder20Population() + "|" +
				"20 - 30대 -> " + populationEntity.getAge2030Population() + "|" +
				"40 - 60대 -> " + populationEntity.getAge4060Population() + "|" +
				"70대 이상 -> " + populationEntity.getAgeOver70Population() + "|\n";
	}

	private String convert(SpotStatEntity spotStatEntity) {

		return "|동 이름 -> " + spotStatEntity.getUmdNm() + "|" +
				"카테고리 명 -> " + spotStatEntity.getSpotType() + "|" +
				"갯수 -> " + spotStatEntity.getCount() + "|\n";
	}

	private String convert(PromptResourceDto.PromptVariable promptVariable) {

		StringBuilder variable = new StringBuilder("|조건 명 -> " + promptVariable.getKey() + "|" +
				"우선순위 -> " + promptVariable.getPriority() + "|" +
				"조건 정보 -> ");
		for (String value : promptVariable.getValues()) variable.append(value).append(",");
		return variable + "|\n";
	}

	public <E> String convertList(String title, List<E> entityList) {

		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append(title).append(" : {");
		for (E entity : entityList) {
			if (entity instanceof HouseInfoEntity) {
				stringBuilder.append(convert((HouseInfoEntity) entity));
			} else if (entity instanceof HouseDealEntity) {
				stringBuilder.append(convert((HouseDealEntity) entity));
			} else if (entity instanceof PopulationEntity) {
				stringBuilder.append(convert((PopulationEntity) entity));
			} else if (entity instanceof SpotStatEntity) {
				stringBuilder.append(convert((SpotStatEntity) entity));
			} else if (entity instanceof PromptResourceDto.PromptVariable) {
				stringBuilder.append(convert((PromptResourceDto.PromptVariable) entity));
			}
		}
		stringBuilder.append("}\n");
		return stringBuilder.toString();
	}
}
