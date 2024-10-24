package com.ssafyhome.config;

import com.ssafyhome.middleware.interceptor.UserProfileInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

  @Value("${front-end.url}")
  String frontEndUrl;

  private final UserProfileInterceptor userProfileInterceptor;

  public WebConfig(UserProfileInterceptor userProfileInterceptor) {

    this.userProfileInterceptor = userProfileInterceptor;
  }

  @Override
  public void configurePathMatch(PathMatchConfigurer configurer) {

    configurer.addPathPrefix("/api", c -> true);
  }

  @Override
  public void addCorsMappings(CorsRegistry registry) {

    registry.addMapping("/**")
        .allowedOrigins(frontEndUrl);
  }

  @Override
  public void addInterceptors(InterceptorRegistry registry) {
    registry
        .addInterceptor(userProfileInterceptor)
        .addPathPatterns("/api/**");
  }
}
