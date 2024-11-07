package com.ssafyhome.config;

import com.ssafyhome.middleware.interceptor.UserProfileInterceptor;
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

  private final UserProfileInterceptor userProfileInterceptor;

  public WebConfig(UserProfileInterceptor userProfileInterceptor) {

    this.userProfileInterceptor = userProfileInterceptor;
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

  /**
   * 로그인 상태인 경우, 로그인 정보를 header에 넣어서 반환하기 위한 인터셉트를 전역으로 설정
   */
  @Override
  public void addInterceptors(InterceptorRegistry registry) {
    registry
        .addInterceptor(userProfileInterceptor)
        .addPathPatterns("/api/**");
  }
}
