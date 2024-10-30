package com.ssafyhome.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

@Configuration
public class ThreadConfig {

    @Bean
    public ExecutorService executorService() {

        return Executors.newVirtualThreadPerTaskExecutor();
    }

    @Bean
    public Semaphore semaphore() {

        return new Semaphore(Runtime.getRuntime().availableProcessors() * 2);
    }
}
