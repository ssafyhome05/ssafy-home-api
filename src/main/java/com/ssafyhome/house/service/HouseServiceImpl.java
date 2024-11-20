package com.ssafyhome.house.service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import com.ssafyhome.common.util.ConvertUtil;
import com.ssafyhome.house.dao.repository.SearchKeywordRepository;
import com.ssafyhome.house.dao.repository.TopTenRepository;
import com.ssafyhome.house.dto.*;
import com.ssafyhome.house.entity.SearchKeywordEntity;
import com.ssafyhome.house.entity.TopTenEntity;
import com.ssafyhome.house.response.SseMessageCode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import com.ssafyhome.common.api.news.NewsClient;
import com.ssafyhome.common.api.news.dto.NewsDto;
import com.ssafyhome.common.exception.GonggongApplicationErrorException;
import com.ssafyhome.house.dao.HouseMapper;
import com.ssafyhome.house.entity.PopulationEntity;

@Service
public class HouseServiceImpl implements HouseService {

	/**
	 * 작업을 비동기로 진행하고 완료가 된 경우, 실시간으로 알림을 사용자에게 전송하기 위해서
	 * SSE protocol (Server Send Event)를 이용함
	 * SSE protocol로 연결 시 클라이언트와 response를 끊지않고 유지하면서 sse emitter가 event를 sent함
	 * WebSocket과의 차이점은 연결을 6개의 클라이언트와 유지할 수 있고 Request는 연결을 유지하지 않음
	 */
	private final Map<String, SseEmitter> sseEmitters = new ConcurrentHashMap<>();

	private final HouseMapper houseMapper;
	private final HouseInternalService houseInternalService;
	private final ExecutorService executorService;
	private final SearchKeywordRepository searchKeywordRepository;
	private final TopTenRepository topTenRepository;
	private final ConvertUtil convertUtil;
	private final NewsClient newsClient;

	public HouseServiceImpl(
			HouseMapper houseMapper,
			HouseInternalService houseInternalService,
			ExecutorService executorService,
			SearchKeywordRepository searchKeywordRepository,
			TopTenRepository topTenRepository,
			ConvertUtil convertUtil,
			NewsClient newsClient
	) {

		this.houseMapper = houseMapper;
		this.houseInternalService = houseInternalService;
		this.executorService = executorService;
		this.searchKeywordRepository = searchKeywordRepository;
		this.topTenRepository = topTenRepository;
		this.convertUtil = convertUtil;
		this.newsClient = newsClient;
	}

	@Override
	public List<HouseDto> getHouseInfo(HouseSearchWithTimeDto searchDto) {

		List<HouseDto> houseInfoList = houseMapper.getHouseInfo(searchDto);

		return houseInfoList;
	}

	@Override
	public List<HouseDealDto> getHouseDealList(String houseSeq, int page, int limit) {

		int offset = page * limit;
		List<HouseDealDto> houseDealList = houseMapper.getHouseDealList(houseSeq, limit, offset);

		return houseDealList;
	}

	@Override
	public List<HouseGraphDto> getHouseGraph(String houseSeq, int year){

		List<HouseGraphDto> houseGraphDtoList = houseMapper.getHouseGraph(houseSeq, year);

		return houseGraphDtoList;
	}

	/**
	 *  모든 행정동 297개에 대한 population 데이터 DB 의 population table 에 저장 하는 메서드
	 *  SGISClient 사용
	 */
	@Override
	public void updatePopulationTask(int year) {

		houseInternalService.getPopulation(year);
	}
	
	@Override
	public String startHouseInfoTask(int dealYmd, int startCd, int endCd) {

		String requestId = UUID.randomUUID().toString();

		SseEmitter sseEmitter = new SseEmitter(Long.MAX_VALUE);
		sseEmitters.put(requestId, sseEmitter);

		ConcurrentHashMap<Integer, Boolean> processingLawdMap = new ConcurrentHashMap<>();

		executorService.submit(() -> {
			try {
				LocalDateTime start = LocalDateTime.now();
				new SseMessageDto<>(
						SseMessageCode.START_TASK,
						start.format(timeFormatter())
				).sendEvent(sseEmitter);

				List<Integer> lawdCdList = houseMapper.getLawdCdList(
						String.valueOf(startCd),
						String.valueOf(endCd)
				);

				int size = lawdCdList.size();

				AtomicInteger seq = new AtomicInteger(1);

				CountDownLatch countDownLatch = new CountDownLatch(size);

				ExecutorService executorService1 = Executors.newFixedThreadPool(20);
				for (int lawdCd : lawdCdList) {

					executorService1.submit(() -> {

						Boolean existingValue = processingLawdMap.putIfAbsent(lawdCd, true);
						if (existingValue != null) return;

						try {
							HouseInfoTask houseInfoTask = null;
							int tryTimes = 5;
							while (tryTimes-- > 0) {
								try {
									houseInfoTask = houseInternalService.insertHouseData(
											lawdCd,
											dealYmd,
											sseEmitters.get(requestId)
									);
									break;
								} catch (GonggongApplicationErrorException e) {
									Thread.sleep(5000);
								} catch (Exception e) {
									throw new Exception(e);
								}
							}
							if (tryTimes == 0 && houseInfoTask == null) {
								new SseMessageDto<>(
										SseMessageCode.API_ERROR,
										new SseMessageDto.Stats(String.valueOf(lawdCd), seq.getAndIncrement(), size)
								).sendEvent(sseEmitter);
							}
							else {
								new SseMessageDto<>(
										SseMessageCode.TASK_COMPLETED,
										new SseMessageDto.Stats(houseInfoTask.getTaskName(), seq.getAndIncrement(), size)
								).sendEvent(sseEmitter);
							}
						} catch (Exception e) {
							System.out.println(e.getMessage());
						} finally {
							countDownLatch.countDown();
						}
					});
				}

				countDownLatch.await();

				LocalDateTime end = LocalDateTime.now();
				new SseMessageDto<>(
						SseMessageCode.TASK_FINISHED,
						end.format(timeFormatter())
				).sendEvent(sseEmitter);

				Duration duration = Duration.between(start, end);
				new SseMessageDto<>(
						SseMessageCode.TASK_SPEND_TIME,
						String.format(
								"%d:%d:%d",
								duration.toHours(),
								duration.toMinutes() - 60 * duration.toHours(),
								duration.toSeconds() - 60 * duration.toMinutes()
						)
				).sendEvent(sseEmitter);
			} catch (Exception e) {
				sseEmitter.completeWithError(e);
			} finally {
				sseEmitters.remove(requestId);
			}
		});

		return requestId;
	}

	@Override
	public SseEmitter getSseEmitter(String requestId) {

		if (!sseEmitters.containsKey(requestId)) {
			throw new NoSuchElementException();
		}
		return sseEmitters.get(requestId);
	}

	private DateTimeFormatter timeFormatter() {

		return DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
	}

	@Override
	public PopulationDto getPopulation(String dongCode) {

		PopulationEntity entity = houseMapper.getPopulation(dongCode);
		int totalPopulation = entity.getAgeUnder20Population() + entity.getAge2030Population() + entity.getAge4060Population() + entity.getAgeOver70Population();

		List<PopulationDto.Generation> generations = new ArrayList<>();
		generations.add(new PopulationDto.Generation("under_20", entity.getAgeUnder20Population(), totalPopulation));
		generations.add(new PopulationDto.Generation("20_30", entity.getAge2030Population(), totalPopulation));
		generations.add(new PopulationDto.Generation("40_60", entity.getAge4060Population(), totalPopulation));
		generations.add(new PopulationDto.Generation("over_70", entity.getAgeOver70Population(), totalPopulation));

		return PopulationDto.builder()
				.dongCode(entity.getDongCode())
				.totalPopulation(totalPopulation)
				.populationDensity(entity.getPpltnDnsty())
				.corpCnt(Integer.parseInt(entity.getCorpCnt()))
				.houseCnt(Integer.parseInt(entity.getTotHouse()))
				.generations(generations)
				.build();
	}

	public void saveSearchKeyword(String dongCode) {
		searchKeywordRepository.save(new SearchKeywordEntity(dongCode, LocalDateTime.now()));
	}

	@Override
	public TopTenDto getTopTen() {

		TopTenEntity topTenEntity = topTenRepository.findLastByOrderByRankTimeDesc().get();
		TopTenDto topTenDto = new TopTenDto(topTenEntity.getRankTime());
		if (topTenEntity.getElements() != null) {

			List<TopTenDto.Element> elements = convertUtil.convert(topTenEntity.getElements().values().stream().toList(), TopTenDto.Element.class);
			elements = elements.stream()
					.sorted(Comparator.comparingInt(TopTenDto.Element::getRank))
					.peek(e -> e.setDongName(houseMapper.getDongNameByCode(e.getKeyword())))
					.toList();
			topTenDto.setElements(elements);
		}

		return topTenDto;
	}

	@Override
	public NewsDto getNews() {
		
		return newsClient.getNews();
	}
}


