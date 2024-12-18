package com.ssafyhome.config;

import com.ssafyhome.common.interceptor.IPInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Global하게 Url과 관련된 설정
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

  @Value("${front-end.url}")
  String frontEndUrl;

  private final IPInterceptor ipInterceptor;

  public WebConfig(IPInterceptor ipInterceptor) {

    this.ipInterceptor = ipInterceptor;
  }

  /**
   * 전역으로 RESTAPI에 api 접두사를 생성
   */
  @Override
  public void configurePathMatch(PathMatchConfigurer configurer) {

    configurer.addPathPrefix("/api", c -> true);
  }

  /**
   * FE와 통신시 Cors정책 허용
   */
  @Override
  public void addCorsMappings(CorsRegistry registry) {

    registry.addMapping("/**")
        .allowedOrigins(frontEndUrl);
  }

  @Override
  public void addInterceptors(InterceptorRegistry registry) {

    registry.addInterceptor(ipInterceptor);
  }
}
