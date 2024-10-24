package com.ssafyhome.model.service.impl;

import com.ssafyhome.model.dao.mapper.HouseMapper;
import com.ssafyhome.model.dto.house.HouseDealsDto;
import com.ssafyhome.model.dto.house.HouseDto;
import com.ssafyhome.exception.GonggongApplicationErrorException;
import com.ssafyhome.model.dto.house.HouseInfoTask;
import com.ssafyhome.model.service.HouseService;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.*;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class HouseServiceImpl implements HouseService {

    private final Map<String, SseEmitter> sseEmitters = new ConcurrentHashMap<>();

    private final HouseMapper houseMapper;
    private final HouseInternalService houseInternalService;

    public HouseServiceImpl(
        HouseMapper houseMapper,
        HouseInternalService houseInternalService
    ) {

      this.houseMapper = houseMapper;
      this.houseInternalService = houseInternalService;
    }

    @Override
    public List<HouseDto> getHouseInfo(String dongCode) {

        List<HouseDto> houseInfoList = houseMapper.getHouseInfo(dongCode);

        return houseInfoList;
    }

    @Override
    public List<HouseDealsDto> getHouseDeals(String houseSeq, int page, int limit) {

        int offset = page * limit;
        List<HouseDealsDto> houseDealsList = houseMapper.getHouseDeals(houseSeq, limit, offset);

        return houseDealsList;
    }

	@Override
	public String startHouseInfoTask(int dealYmd, int startCd, int endCd) {

		String requestId = UUID.randomUUID().toString();


		SseEmitter sseEmitter = new SseEmitter(Long.MAX_VALUE);
		sseEmitters.put(requestId, sseEmitter);

		ExecutorService executorService = Executors.newVirtualThreadPerTaskExecutor();

		int maxConcurrentThreads = 10;
		Semaphore semaphore = new Semaphore(maxConcurrentThreads);

		ConcurrentHashMap<Integer, Boolean> processingLawdMap = new ConcurrentHashMap<>();

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

				List<Thread> threadList = new ArrayList<>();
				CountDownLatch countDownLatch = new CountDownLatch(size);

				for (int lawdCd : lawdCdList) {

					Thread vThread = Thread.startVirtualThread(() -> {

						try {
							Boolean existingValue = processingLawdMap.putIfAbsent(lawdCd, true);
							if (existingValue != null) return;

							semaphore.acquire();

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

							sseEmitter.send(SseEmitter.event()
									.name(houseInfoTask.getTaskName())
									.data(houseInfoTask.getTotalRows() + " rows completed!! commit (" + seq.getAndIncrement() + "/" + size + ") " + houseInfoTask.getDuration().toSeconds() + " seconds")
							);

						} catch (Exception e) {
							throw new CompletionException(e);
						} finally {

							semaphore.release();
							countDownLatch.countDown();
						}
					});
					threadList.add(vThread);
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


