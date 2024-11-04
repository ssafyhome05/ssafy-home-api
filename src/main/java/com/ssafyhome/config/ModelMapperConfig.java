package com.ssafyhome.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * ModelMapper 빈으로 등록
 *
 * 어디서 쓰냐?
 * ConvertUtil - 같은 필드명을 가진 경우에 해당 값을 매핑해주는 클래스
 */
@Configuration
public class ModelMapperConfig {

    @Bean
    public ModelMapper modelMapper() {

        return new ModelMapper();
    }
}
