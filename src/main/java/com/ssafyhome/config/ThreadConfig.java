package com.ssafyhome.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * ExecutorService를 Bean으로 관리하기 위한 설정
 *
 * 그래서 이게 뭔데?
 * 자바에서 비동기 작업을 처리하기 위해서 스레드를 관리하기 위해 제공되는 서비스
 * Virtual Thread를 사용하도록 빈으로 설정하고 생명주기를 스프링이 관리하도록 함
 */
@Configuration
public class ThreadConfig {

    @Bean
    public ExecutorService executorService() {

        return Executors.newVirtualThreadPerTaskExecutor();
    }
}
