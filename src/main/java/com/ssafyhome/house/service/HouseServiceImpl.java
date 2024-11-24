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

import com.ssafyhome.common.api.sgis.SGISClient;
import com.ssafyhome.common.api.sgis.SGISUtil;
import com.ssafyhome.common.api.sgis.dto.SgisPopulation;
import com.ssafyhome.common.api.sgis.dto.SgisSearchPopulation;
import com.ssafyhome.common.mapper.GeometryMapper;
import com.ssafyhome.common.util.ConvertUtil;
import com.ssafyhome.common.util.GeometryUtil;
import com.ssafyhome.common.util.object.Point;
import com.ssafyhome.house.code.AdmCode;
import com.ssafyhome.house.code.AgeCode;
import com.ssafyhome.house.dao.repository.SearchKeywordRepository;
import com.ssafyhome.house.dao.repository.TopTenRepository;
import com.ssafyhome.house.dto.*;
import com.ssafyhome.house.entity.AdminLogsEntity;
import com.ssafyhome.house.entity.SearchKeywordEntity;
import com.ssafyhome.house.entity.TopTenEntity;
import com.ssafyhome.house.exception.RequestIdNotFoundException;
import com.ssafyhome.house.code.SseMessageCode;
import lombok.Synchronized;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import com.ssafyhome.common.api.news.NewsClient;
import com.ssafyhome.common.exception.GonggongApplicationErrorException;
import com.ssafyhome.house.dao.HouseMapper;
import com.ssafyhome.house.entity.PopulationEntity;

@Service
public class HouseServiceImpl implements HouseService {

	@Value("${server.ip}")
	private String serverIp;

	@Value("${server.port}")
	private String serverPort;

	@Value("${spring.datasource.hikari.maximum-pool-size")
	private String maxPoolSize;

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
	private final GeometryUtil geometryUtil;
	private final GeometryMapper geometryMapper;
	private final SGISClient sgisClient;
	private final SGISUtil sgisUtil;

	public HouseServiceImpl(
			HouseMapper houseMapper,
			HouseInternalService houseInternalService,
			ExecutorService executorService,
			SearchKeywordRepository searchKeywordRepository,
			TopTenRepository topTenRepository,
			ConvertUtil convertUtil,
			NewsClient newsClient,
			GeometryUtil geometryUtil,
			GeometryMapper geometryMapper,
			SGISClient sgisClient,
			SGISUtil sgisUtil
	) {

		this.houseMapper = houseMapper;
		this.houseInternalService = houseInternalService;
		this.executorService = executorService;
		this.searchKeywordRepository = searchKeywordRepository;
		this.topTenRepository = topTenRepository;
		this.convertUtil = convertUtil;
		this.newsClient = newsClient;
		this.geometryUtil = geometryUtil;
		this.geometryMapper = geometryMapper;
		this.sgisClient = sgisClient;
		this.sgisUtil = sgisUtil;
	}
	
	@Override
	public String startHouseInfoTask(int dealYmd, int startCd, int endCd, String clientIpv4, String clientIpv6) {

		String requestId = UUID.randomUUID().toString();
		SseEmitter sseEmitter = new SseEmitter(Long.MAX_VALUE);
		sseEmitters.put(requestId, sseEmitter);
		ConcurrentHashMap<Integer, Boolean> processingLawdMap = new ConcurrentHashMap<>();

		executorService.submit(() ->
				asyncHouseInfoTask(
					sseEmitter,
					requestId,
					processingLawdMap,
					dealYmd,
					String.valueOf(startCd),
					String.valueOf(endCd),
					clientIpv4,
					clientIpv6
				)
		);

		return serverIp
				+ ":"
				+ serverPort
				+ "/api/house/task/"
				+ requestId
				+ "/status";
	}

	private void asyncHouseInfoTask(
			SseEmitter sseEmitter,
			String requestId,
			Map<Integer, Boolean> processingLawdMap,
			int dealYmd,
			String startCd,
			String endCd,
			String clientIpv4,
			String clientIpv6
	) {

		try {

			LocalDateTime start = LocalDateTime.now();
			new SseMessageDto<>(
					SseMessageCode.START_TASK,
					start.format(timeFormatter())
			).sendEvent(sseEmitter);

			List<Integer> lawdCdList = houseMapper.getLawdCdList(startCd, endCd);
			int size = lawdCdList.size();
			AtomicInteger seq = new AtomicInteger(1);
			CountDownLatch countDownLatch = new CountDownLatch(size);

      try (ExecutorService fixedExecutorService = Executors.newFixedThreadPool(Integer.parseInt(maxPoolSize))) {

        for (int lawdCd : lawdCdList) {
          fixedExecutorService.submit(() ->
							fixedPoolHouseInfoMultiTask(
									sseEmitter,
									requestId,
									processingLawdMap,
									dealYmd,
									lawdCd,
									countDownLatch,
									seq,
									size
							)
					);
        }
      }

      countDownLatch.await();

			LocalDateTime end = LocalDateTime.now();
			new SseMessageDto<>(
					SseMessageCode.TASK_FINISHED,
					end.format(timeFormatter())
			).sendEvent(sseEmitter);

			new SseMessageDto<>(
					SseMessageCode.TASK_SPEND_TIME,
					calcDuration(start, end)
			).sendEvent(sseEmitter);

		} catch (Exception e) {

			sseEmitter.completeWithError(e);

		} finally {

			sseEmitters.remove(requestId);
			AdminLogsEntity adminLogsEntity = new AdminLogsEntity();
			adminLogsEntity.setAdminSeq(Integer.parseInt(SecurityContextHolder.getContext().getAuthentication().getName()));
			adminLogsEntity.setTaskName("HOUSE_INFO");
			adminLogsEntity.setTaskData(String.valueOf(dealYmd));
			adminLogsEntity.setIpv4(clientIpv4);
			adminLogsEntity.setIpv6(clientIpv6);
			houseMapper.insertUpdateLogs(adminLogsEntity);
		}
	}

	private void fixedPoolHouseInfoMultiTask(
			SseEmitter sseEmitter,
			String requestId,
			Map<Integer, Boolean> processingLawdMap,
			int dealYmd,
			int lawdCd,
			CountDownLatch countDownLatch,
			AtomicInteger seq,
			int size
	) {

		Boolean existingValue = processingLawdMap.putIfAbsent(lawdCd, true);
		if (existingValue != null) return;

		try {

			int tryTimes = 5;
			while (--tryTimes >= 0) {
				try {
					houseInternalService.insertHouseData(
							lawdCd,
							dealYmd,
							sseEmitters.get(requestId)
					);
					break;

				} catch (GonggongApplicationErrorException e) {

					Thread.sleep(5000);

				} catch (Exception e) {

					//추가 정리 필요
					throw new Exception(e);
				}
			}

			if (tryTimes < 0) {

				new SseMessageDto<>(
						SseMessageCode.API_ERROR,
						new SseMessageDto.Stats(String.valueOf(lawdCd), seq.getAndIncrement(), size)
				).sendEvent(sseEmitter);

			} else {

				new SseMessageDto<>(
						SseMessageCode.TASK_COMPLETED,
						new SseMessageDto.Stats(lawdCd + "-" + dealYmd, seq.getAndIncrement(), size)
				).sendEvent(sseEmitter);

			}

		} catch (Exception e) {

			System.out.println(e.getMessage());

		} finally {

			countDownLatch.countDown();
		}
	}

	public String calcDuration(LocalDateTime start, LocalDateTime end) {

		Duration duration = Duration.between(start, end);
		return String.format(
				"%d:%d:%d",
				duration.toHours(),
				duration.toMinutes() - 60 * duration.toHours(),
				duration.toSeconds() - 60 * duration.toMinutes()
		);
	}

	@Override
	public SseEmitter getSseEmitter(String requestId) {

		if (!sseEmitters.containsKey(requestId)) {
			throw new RequestIdNotFoundException(requestId);
		}
		return sseEmitters.get(requestId);
	}

	private DateTimeFormatter timeFormatter() {

		return DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
	}

	/**
	 *  모든 행정동 297개에 대한 population 데이터 DB 의 population table 에 저장 하는 메서드
	 *  SGISClient 사용
	 */
	@Override
	@Synchronized
	public AdminUpdatedInfoDto updatePopulation(int year, String clientIpv4, String clientIpv6) {

		List<Integer> admCdList = AdmCode.getAllCodes();
		List<PopulationEntity> populationList = new ArrayList<>();
		CountDownLatch countDownLatch = new CountDownLatch(admCdList.size());

		for (int admCd : admCdList) {
			executorService.execute(() -> {
				try {
					populationList.addAll(populationMultiTask(year, admCd));
				}
				finally {
					countDownLatch.countDown();
				}
			});
		}

		try {
			countDownLatch.await();
		} catch (InterruptedException e) {
		}

		houseMapper.insertPopulation(populationList);
		AdminLogsEntity adminLogsEntity = new AdminLogsEntity();
		adminLogsEntity.setAdminSeq(Integer.parseInt(SecurityContextHolder.getContext().getAuthentication().getName()));
		adminLogsEntity.setTaskName("POPULATION_INFO");
		adminLogsEntity.setTaskData(String.valueOf(year));
		adminLogsEntity.setIpv4(clientIpv4);
		adminLogsEntity.setIpv6(clientIpv6);
		houseMapper.insertUpdateLogs(adminLogsEntity);
		return convertUtil.convert(adminLogsEntity, AdminUpdatedInfoDto.class);
	}

	private List<PopulationEntity> populationMultiTask(
			int year,
			int admCd
	) {

		SgisPopulation populationCode = sgisClient.getPopulation(
				sgisUtil.getAccessToken(),
				year,
				admCd,
				1
		);

		List<PopulationEntity> tempList = convertUtil.convert(populationCode.getResult(), PopulationEntity.class);
		Map<String, SearchPopulationDto> map = getSearchPopulation(year, admCd);
		tempList.forEach(entity -> {
			SearchPopulationDto defaultDto = new SearchPopulationDto();
			entity.setAgeUnder20Population(map.getOrDefault(entity.getAdmCd(), defaultDto).getAgeUnder20Population().get());
			entity.setAge2030Population(map.getOrDefault(entity.getAdmCd(), defaultDto).getAge2030Population().get());
			entity.setAge4060Population(map.getOrDefault(entity.getAdmCd(), defaultDto).getAge4060Population().get());
			entity.setAgeOver70Population(map.getOrDefault(entity.getAdmCd(), defaultDto).getAgeOver70Population().get());
		});

		return tempList;
	}

	private Map<String, SearchPopulationDto> getSearchPopulation(int year, int admCd) {

		Map<String, SearchPopulationDto> searchPopulationMap = new ConcurrentHashMap<>();
		CountDownLatch countDownLatch = new CountDownLatch(AgeCode.values().length);

		for(AgeCode THIS_CODE : AgeCode.values() ) {
			executorService.execute(() -> {
				try{
					SgisSearchPopulation sgisSearchPopulation = sgisClient.getSearchPopulation(
							sgisUtil.getAccessToken(),
							year,
							admCd,
							THIS_CODE.getCode(),
							1
					);
					sgisSearchPopulation.getResult().forEach(result -> {
						searchPopulationMap.putIfAbsent(result.getAdmCd(), new SearchPopulationDto());
						searchPopulationMap.get(result.getAdmCd()).setPopulationByAge(THIS_CODE.getGeneration(), result.getPopulation());
					});
				}
				finally {
					countDownLatch.countDown();
				}
			});
		}

		try {
			countDownLatch.await();
		}
		catch (InterruptedException e) {
		}
		return searchPopulationMap;
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

	@Override
	public void saveSearchKeyword(String dongCode, String clientIp) {

		searchKeywordRepository.save(new SearchKeywordEntity(dongCode, LocalDateTime.now(), clientIp));
	}

	@Override
	public TopTenDto getTopTen() {

		TopTenEntity topTenEntity = topTenRepository.findLastByOrderByRankTimeDesc().get();
		return parseTopTenEntity(topTenEntity);
	}

	private TopTenDto parseTopTenEntity(TopTenEntity topTenEntity) {

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
	public List<HouseDto> getHouseInfo(HouseSearchWithTimeDto searchDto) {

		return houseMapper.getHouseInfo(searchDto);
	}

	@Override
	public List<HouseDealDto> getHouseDealList(String houseSeq, int page, int limit) {

		return houseMapper.getHouseDealList(houseSeq, limit, page * limit);
	}

	@Override
	public List<HouseGraphDto> getHouseGraph(String houseSeq, int year){

		return houseMapper.getHouseGraph(houseSeq, year);
	}

	@Override
	public List<NewsDto> getNewsList() {
		
		return houseMapper.getNewsList();
	}

	@Override
	public List<Point> getPoints(String dongCode) {

		return geometryUtil.getPoints(geometryMapper.selectByDongCode(dongCode));
	}

	@Override
	public AdminUpdatedInfoDto getRecentLoginInfo(String taskName) {

		return convertUtil.convert(houseMapper.getUpdatedLogs(taskName), AdminUpdatedInfoDto.class);
	}
}


