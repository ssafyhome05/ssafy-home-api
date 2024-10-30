package com.ssafyhome;

import com.ssafyhome.model.service.SpotService;
import com.ssafyhome.model.service.impl.SpotInternalService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@SpringBootTest
public class RedisLockTest {

    private final SpotInternalService spotInternalService;
    private final SpotService spotService;

    @Autowired
    public RedisLockTest(
            SpotInternalService spotInternalService,
            SpotService spotService
    ) {

        this.spotInternalService = spotInternalService;
        this.spotService = spotService;
    }

    @Test
    public void testLock() throws InterruptedException {

        ExecutorService executorService = Executors.newFixedThreadPool(10);
        CountDownLatch latch = new CountDownLatch(20);

        for (int i = 0; i < 20; i++) {
            executorService.submit(() -> {
                try {
                    spotInternalService.getSpotsFromAPI("123123");
                } catch (Exception e) {}
                finally {
                    latch.countDown();
                }
            });
        }

        latch.await();
    }

    @Test
    public void insertSpotTest() throws InterruptedException {

        ExecutorService executorService = Executors.newFixedThreadPool(10);
        CountDownLatch latch = new CountDownLatch(20);

        for (int i = 0; i < 20; i++) {
            executorService.submit(() -> {
                try {
                    System.out.println(spotService.getSpotsByDongCode("1168010100"));
                } catch (Exception e) {}
                finally {
                    latch.countDown();
                }
            });
        }

        latch.await();
    }
}