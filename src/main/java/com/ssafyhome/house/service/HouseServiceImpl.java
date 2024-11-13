package com.ssafyhome.house.service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicInteger;

import com.ssafyhome.house.dto.HouseGraphDto;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import com.ssafyhome.common.exception.GonggongApplicationErrorException;
import com.ssafyhome.house.dao.HouseMapper;
import com.ssafyhome.house.dto.HouseDealDto;
import com.ssafyhome.house.dto.HouseDto;
import com.ssafyhome.house.dto.HouseInfoTask;

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

	public HouseServiceImpl(
			HouseMapper houseMapper,
			HouseInternalService houseInternalService,
			ExecutorService executorService
	) {

		this.houseMapper = houseMapper;
		this.houseInternalService = houseInternalService;
		this.executorService = executorService;
	}

	@Override
	public List<HouseDto> getHouseInfo(Map<String, Object> params) {

		List<HouseDto> houseInfoList = houseMapper.getHouseInfo(params);

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

	@Override
	public String startHouseInfoTask(int dealYmd, int startCd, int endCd) {

		String requestId = UUID.randomUUID().toString();

		SseEmitter sseEmitter = new SseEmitter(Long.MAX_VALUE);
		sseEmitters.put(requestId, sseEmitter);

		ConcurrentHashMap<Integer, Boolean> processingLawdMap = new ConcurrentHashMap<>();
		Semaphore semaphore = new Semaphore(20);

		executorService.submit(() -> {
			try {
				LocalDateTime start = LocalDateTime.now();
				sseEmitter.send(SseEmitter.event()
						.name("Task start time")
						.data(start.format(timeFormatter()))
				);

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
											sseEmitters.get(requestId),
											semaphore
									);
									break;
								} catch (GonggongApplicationErrorException e) {
									Thread.sleep(5000);
								} catch (Exception e) {
									throw new Exception(e);
								}
							}
							if (tryTimes == 0 && houseInfoTask == null) {
								sseEmitter.send(SseEmitter.event()
										.name(lawdCd+ "gonggong Error")
										.data( "error (" + seq.getAndIncrement() + "/" + size + ")")

								);
							}
							else {
								sseEmitter.send(SseEmitter.event()
										.name(houseInfoTask.getTaskName())
										.data(houseInfoTask.getTotalRows() + " rows completed!! commit (" + seq.getAndIncrement() + "/" + size + ") " + houseInfoTask.getDuration().toSeconds() + " seconds")
								);
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
				sseEmitter.send(SseEmitter.event()
						.name("Task end time")
						.data(end.format(timeFormatter()))
				);

				Duration duration = Duration.between(start, end);
				sseEmitter.send(SseEmitter.event()
						.name("Task duration")
						.data(
								String.format(
										"%d:%d:%d",
										duration.toHours(),
										duration.toMinutes() - 60 * duration.toHours(),
										duration.toSeconds() - 60 * duration.toMinutes()
								)
						)
				);
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

}


