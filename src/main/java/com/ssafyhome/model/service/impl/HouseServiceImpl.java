package com.ssafyhome.model.service.impl;

import com.ssafyhome.exception.GonggongApplicationErrorException;
import com.ssafyhome.model.dao.mapper.HouseMapper;
import com.ssafyhome.model.dto.house.HouseInfoTask;
import com.ssafyhome.model.service.HouseService;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class HouseServiceImpl implements HouseService {

	private final Map<String, HouseInfoTask> sseEmitters = new ConcurrentHashMap<>();

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
	public String startHouseInfoTask(int dealYmd, int startCd, int endCd) {

		String requestId = UUID.randomUUID().toString();


		SseEmitter sseEmitter = new SseEmitter(Long.MAX_VALUE);
		sseEmitters.put(requestId, new HouseInfoTask(sseEmitter));

		CompletableFuture.runAsync(() -> {
			try {
				LocalDateTime start = LocalDateTime.now();
				sseEmitter.send(SseEmitter.event()
						.name("Task start time")
						.data(start.format(timeFormatter()))
				);
				List<Integer> lawdCdList = houseMapper.getLawdCdList(String.valueOf(startCd), String.valueOf(endCd));
				int seq = 1;
				int size = lawdCdList.size();
				for (int lawdCd : lawdCdList) {
					Duration partTime = null;
					int tryTimes = 5;
					while(tryTimes-- > 0) {
						try {
							partTime = houseInternalService.insertHouseData(lawdCd, dealYmd, sseEmitters.get(requestId));
							break;
						} catch(GonggongApplicationErrorException e) {
							Thread.sleep(5000);
						} catch (Exception e) {
							throw new Exception();
						}
					}
					sseEmitter.send(SseEmitter.event()
							.name(sseEmitters.get(requestId).getTaskName())
							.data("Task completed!! commit (" + seq++ + "/" + size + ") " + partTime.toSeconds() + " seconds")
					);
				}
				LocalDateTime end = LocalDateTime.now();
				sseEmitter.send(SseEmitter.event()
						.name("Task end time")
						.data(end.format(timeFormatter()))
				);
				Duration duration = Duration.between(start, end);
				sseEmitter.send(SseEmitter.event()
						.name("Task duration")
						.data(String.format("%d:%d:%d", duration.toHours(), duration.toMinutes() - 60 * duration.toHours(), duration.toSeconds() - 60 * duration.toMinutes()))
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
	public SseEmitter getHouseInfoTask(String requestId) {

		if (!sseEmitters.containsKey(requestId)) {
			throw new NoSuchElementException();
		}
		return sseEmitters.get(requestId).getEmitter();
	}

	private DateTimeFormatter timeFormatter() {

		return DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
	}
}


