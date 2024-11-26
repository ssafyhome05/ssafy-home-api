package com.ssafyhome.bookmark.service;

import com.ssafyhome.bookmark.dao.BookmarkMapper;
import com.ssafyhome.bookmark.dto.BookmarkStatusDto;
import com.ssafyhome.common.api.tmap.dto.TMapPoint;
import com.ssafyhome.house.dto.HouseDto;
import com.ssafyhome.house.entity.HouseInfoEntity;
import com.ssafyhome.house.service.HouseService;
import com.ssafyhome.navigate.dto.NavigateDto;
import com.ssafyhome.navigate.service.NavigateService;
import com.ssafyhome.spot.dto.CustomSpotDto;
import com.ssafyhome.spot.dto.LocationDto;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;

@Service
public class BookmarkServiceImpl implements BookmarkService {

    private final BookmarkMapper bookmarkMapper;
    private final HouseService houseService;
    private final NavigateService navigateService;
		private final ExecutorService executorService;

    public BookmarkServiceImpl(
            BookmarkMapper bookmarkMapper,
            HouseService houseService,
            NavigateService navigateService,
            ExecutorService executorService
    ) {

        this.bookmarkMapper = bookmarkMapper;
        this.houseService = houseService;
        this.navigateService = navigateService;
				this.executorService = executorService;
    }

  @Override
  public BookmarkStatusDto getBookmarkStatus() {

      BookmarkStatusDto bookmarkStatusDto = new BookmarkStatusDto();

      List<LocationDto> dongCodeList = getLocationList();
      List<CustomSpotDto> customSpotDtoList = getCustomSpotList();
      List<HouseDto> houseDtoList = getHouseList();

			CountDownLatch countDownLatch = new CountDownLatch(dongCodeList.size() + customSpotDtoList.size());

      bookmarkStatusDto.setPopulations(new HashMap<>());
      bookmarkStatusDto.setCustomSpotRank(new HashMap<>());

      for (LocationDto locationDto : dongCodeList) {
	      executorService.execute(() -> {
		      try{

			      bookmarkStatusDto.getPopulations().put(
					      locationDto.getDongCode(),
					      new BookmarkStatusDto.PopulationWithLocation(
							      locationDto,
							      houseService.getPopulation(locationDto.getDongCode())
					      )
			      );

		      } finally {
			      countDownLatch.countDown();
		      }
	      });
      }

      for (CustomSpotDto customSpotDto : customSpotDtoList) {

	      executorService.execute(() -> {
		      try{

			      List<BookmarkStatusDto.RankItem> rank = new ArrayList<>();
						CountDownLatch innerCountDownLatch = new CountDownLatch(houseDtoList.size());

			      for (HouseDto houseDto : houseDtoList) {

				      executorService.execute(() -> {
					      try {

						      NavigateDto navigateDto = navigateService.getNavigate(
								      "custom",
								      houseDto.getAptSeq(),
								      TMapPoint.builder()
										      .x(Double.parseDouble(customSpotDto.getLongitude()))
										      .y(Double.parseDouble(customSpotDto.getLatitude()))
										      .name(customSpotDto.getSpotName())
										      .build()
						      );

						      rank.add(
								      BookmarkStatusDto.RankItem.builder()
										      .address(houseDto.getJibun())
										      .houseName(houseDto.getAptNm())
										      .houseSeq(houseDto.getAptSeq())
										      .carTime(navigateDto.getRoutes().get("car").getFirst().getTotalTime())
										      .walkTime(navigateDto.getRoutes().get("walk").getFirst().getTotalTime())
										      .transportTime(navigateDto.getRoutes().get("transport").getFirst().getTotalTime())
										      .build()
						      );

					      } catch (Exception e) {
					      } finally {
						      innerCountDownLatch.countDown();
					      }
				      });
			      }

			      try {
				      innerCountDownLatch.await();
			      } catch (InterruptedException e) {}

			      rank.sort(BookmarkStatusDto.RankItem::compareTo);
			      bookmarkStatusDto.getCustomSpotRank().put(customSpotDto.getSpotName(), rank);

		      } finally {
			      countDownLatch.countDown();
		      }
	      });
      }

			try {
				countDownLatch.await();
			} catch (InterruptedException e) {}

      return bookmarkStatusDto;
  }

  @Override
    public void addHouseBookmark(Map<String, Object> params) {
        bookmarkMapper.addHouseBookmark(params);
    }

    @Override
    public void deleteHouseBookmark(Map<String, Object> params) {
        bookmarkMapper.deleteHouseBookmark(params);
    }

    @Override
    public void addLocationBookmark(Map<String, Object> params) {
        bookmarkMapper.addLocationBookmark(params);
    }

    @Override
    public void deleteLocationBookmark(Map<String, Object> params) {
        bookmarkMapper.deleteLocationBookmark(params);
    }

	@Override
	public void addCustomSpotBookmark(Map<String, Object> params) {
		bookmarkMapper.addCustomSpotBookmark(params);
		
	}

	@Override
	public List<HouseDto> getHouseList() {
		// TODO Auto-generated method stub
    String userSeq = SecurityContextHolder.getContext().getAuthentication().getName();
		return bookmarkMapper.getHouseList(userSeq);
	}

	@Override
	public List<LocationDto> getLocationList() {
		// TODO Auto-generated method stub
    String userSeq = SecurityContextHolder.getContext().getAuthentication().getName();
		return bookmarkMapper.getLocationList(userSeq);
	}

	@Override
	public List<CustomSpotDto> getCustomSpotList() {
		// TODO Auto-generated method stub
    String userSeq = SecurityContextHolder.getContext().getAuthentication().getName();
		return bookmarkMapper.getCustomSpotList(userSeq);
	}

	@Override
	public void deleteCustomSpotBookmark(Map<String, Object> params) {
		bookmarkMapper.deleteCustomSpotBookmark(params);
		
	}

}
