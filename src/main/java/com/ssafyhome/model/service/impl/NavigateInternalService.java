package com.ssafyhome.model.service.impl;

import com.ssafyhome.api.kakao.KakaoClient;
import com.ssafyhome.model.dao.mapper.SpotMapper;
import com.ssafyhome.model.dto.api.KakaoPlaceDto;
import com.ssafyhome.model.dto.api.TMapPoint;
import com.ssafyhome.model.entity.mysql.CategoryEntity;
import com.ssafyhome.model.entity.mysql.NearestSpotEntity;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class NavigateInternalService {

    private final String COMPLETE_KEY_FORMAT = "NEAREST_API_TASK:%s:COMPLETE";
    private final String LOCK_KEY_FORMAT = "NEAREST_API_TASK:%s:LOCK";

    private final RedisTemplate<String, String> redisTemplate;
    private final RedissonClient redissonClient;
    private final KakaoClient kakaoClient;
    private final SpotMapper spotMapper;
    private final ExecutorService executorService;

    public NavigateInternalService(
            RedisTemplate<String, String> redisTemplate,
            RedissonClient redissonClient,
            KakaoClient kakaoClient,
            SpotMapper spotMapper,
            ExecutorService executorService
    ) {

        this.redisTemplate = redisTemplate;
        this.redissonClient = redissonClient;
        this.kakaoClient = kakaoClient;
        this.spotMapper = spotMapper;
        this.executorService = executorService;
    }

    @Transactional
    public void findNearestSpot(String aptSeq, TMapPoint start) throws InterruptedException {

        String TASK_COMPLETE_KEY = String.format(COMPLETE_KEY_FORMAT, aptSeq);
        String TASK_LOCK_KEY = String.format(LOCK_KEY_FORMAT, aptSeq);

        if (isCompleted(TASK_COMPLETE_KEY)) {
            log.info(aptSeq + " is already updated");
            return;
        }

        RLock lock = redissonClient.getLock(TASK_LOCK_KEY);
        if (lock.tryLock(10, 30, TimeUnit.SECONDS)) {
            log.info(aptSeq + "lock acquired");
            if (isCompleted(TASK_COMPLETE_KEY)) {
                log.info(aptSeq + " is already updated");
            }
            else {
                log.info(aptSeq + " save task starting");
                List<NearestSpotEntity> nearestSpotEntityList = new ArrayList<>();
                Map<String, KakaoPlaceDto> kakaoPlaceDtoMap = connectWithKakaoAPI(start);

                int size = kakaoPlaceDtoMap.values().stream().mapToInt(list -> list.getDocuments().size()).sum();
                ConcurrentHashMap<String, Boolean> processingSpotMap = new ConcurrentHashMap<>();
                CountDownLatch countDownLatch = new CountDownLatch(size);

                for (String key : kakaoPlaceDtoMap.keySet()) {
                    for(KakaoPlaceDto.Document document : kakaoPlaceDtoMap.get(key).getDocuments()) {
                        executorService.submit(() -> {
                            Boolean existingValue = processingSpotMap.putIfAbsent(document.getId(), true);
                            if (existingValue != null) return;

                            try {
                                nearestSpotEntityList.add(convertToSpotEntity(document, aptSeq, key));
                            } catch (Exception e) {
                                e.printStackTrace();
                            } finally {
                                countDownLatch.countDown();
                            }
                        });
                    }
                }

                try {
                    countDownLatch.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                spotMapper.insertNearestSpots(nearestSpotEntityList);
                redisTemplate.opsForValue().set(
                        TASK_COMPLETE_KEY,
                        String.valueOf(LocalDateTime.now()),
                        10, TimeUnit.DAYS
                );
            }
            lock.unlock();
        }
    }

    private boolean isCompleted(String TASK_COMPLETE_KEY) {

        Boolean isCompleted = redisTemplate.hasKey(TASK_COMPLETE_KEY);
        return Boolean.TRUE.equals(isCompleted);
    }

    private Map<String, KakaoPlaceDto> connectWithKakaoAPI(TMapPoint start) {

        Map<String, KakaoPlaceDto> kakaoPlaceDtoMap = new HashMap<>();

        for (CategoryEntity category : spotMapper.getCategories()) {
            KakaoPlaceDto kakaoPlaceDto = null;
            if (category.isCategoryType()) {
                kakaoPlaceDto = kakaoClient.searchCategoryPlace(
                        category.getCategoryCode(),
                        start.getX(),
                        start.getY(),
                        2000,
                        1,
                        "distance"
                );
            }
            else {
                kakaoPlaceDto = kakaoClient.searchKeywordPlace(
                        category.getCategoryCode(),
                        start.getX(),
                        start.getY(),
                        2000,
                        1,
                        "distance"
                );
            }
            kakaoPlaceDtoMap.put(category.getCategoryName(), kakaoPlaceDto);
        }

        return kakaoPlaceDtoMap;
    }

    private NearestSpotEntity convertToSpotEntity(KakaoPlaceDto.Document document, String aptSeq, String category) {

        return NearestSpotEntity.builder()
                .categoryName(category)
                .aptSeq(aptSeq)
                .spotName(document.getPlaceName())
                .longitude(document.getX())
                .latitude(document.getY())
                .build();
    }

}
