package com.ssafyhome.bookmark.dto;

import com.ssafyhome.house.dto.PopulationDto;
import com.ssafyhome.spot.dto.LocationDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class BookmarkStatusDto {

	private Map<String, PopulationWithLocation> populations;
	private Map<String, List<RankItem>> customSpotRank;

	@Data
	@AllArgsConstructor
	public static class PopulationWithLocation {

		private LocationDto location;
		private PopulationDto population;
	}

	@Data
	@Builder
	public static class RankItem implements Comparable<RankItem> {

		private String houseSeq;
		private String houseName;
		private String address;
		private int carTime;
		private int walkTime;
		private int transportTime;

		@Override
		public int compareTo(RankItem o) {
			return this.transportTime - o.transportTime;
		}
	}
}
