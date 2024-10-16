package com.ssafyhome.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssafyhome.handler.CustomOAuth2SuccessHandler;
import com.ssafyhome.middleware.filter.CustomLoginFilter;
import com.ssafyhome.middleware.filter.CustomLogoutFilter;
import com.ssafyhome.middleware.filter.JWTFilter;
import com.ssafyhome.model.dao.mapper.UserMapper;
import com.ssafyhome.model.dao.repository.RefreshTokenRepository;
import com.ssafyhome.model.service.JWTService;
import com.ssafyhome.model.service.impl.CustomOAuth2UserService;
import com.ssafyhome.model.service.impl.CustomUserDetailsService;
import com.ssafyhome.util.CookieUtil;
import com.ssafyhome.util.ErrorUtil;
import com.ssafyhome.util.JWTUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.web.cors.CorsConfiguration;

import java.util.Collections;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

  @Value("${front-end.url}")
  private String frontEndUrl;

  private final AuthenticationConfiguration authenticationConfiguration;
  private final RefreshTokenRepository refreshTokenRepository;
  private final UserMapper userMapper;
  private final JWTService jwtService;
  private final JWTUtil jwtUtil;
  private final CookieUtil cookieUtil;
  private final ErrorUtil errorUtil;
  private final CustomUserDetailsService userDetailsService;

  public SecurityConfig(
      AuthenticationConfiguration authenticationConfiguration,
      RefreshTokenRepository refreshTokenRepository,
      UserMapper userMapper,
      JWTService jwtService,
      JWTUtil jwtUtil,
      CookieUtil cookieUtil,
      ErrorUtil errorUtil,
      CustomUserDetailsService userDetailsService
  ) {

    this.authenticationConfiguration = authenticationConfiguration;
    this.refreshTokenRepository = refreshTokenRepository;
    this.userMapper = userMapper;
    this.jwtService = jwtService;
    this.jwtUtil = jwtUtil;
    this.cookieUtil = cookieUtil;
    this.errorUtil = errorUtil;
    this.userDetailsService = userDetailsService;
  }

  @Bean
  public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {

    return authenticationConfiguration.getAuthenticationManager();
  }

  @Bean
  public BCryptPasswordEncoder passwordEncoder() {

    return new BCryptPasswordEncoder();
  }

  @Bean
  public RoleHierarchy roleHierarchy() {

    String hierarchy = "ROLE_ADMIN > ROLE_MANAGER > ROLE_USER";
    return RoleHierarchyImpl.fromHierarchy(hierarchy);
  }

  @Bean
  public AccessDeniedHandler customAccessDeniedHandler() {

    return (request, response, accessDeniedException) -> {
      response.setStatus(HttpServletResponse.SC_FORBIDDEN);
      response.setContentType("application/json;charset=UTF-8");

      ObjectMapper mapper = new ObjectMapper();
      String jsonResponse = mapper.writeValueAsString(errorUtil.getErrorDetails(request, accessDeniedException));
      response.getWriter().write(jsonResponse);
    };
  }

  @Bean
  public AuthenticationEntryPoint customAuthenticationEntryPoint() {

    return (request, response, authException) -> {
      response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
      response.setContentType("application/json;charset=UTF-8");

      ObjectMapper mapper = new ObjectMapper();
      String jsonResponse = mapper.writeValueAsString(errorUtil.getErrorDetails(request, authException));
      response.getWriter().write(jsonResponse);
    };
  }

  @Bean
  public SecurityFilterChain securityFilterChain(
      HttpSecurity http,
      CustomOAuth2UserService customOAuth2UserService,
      CustomOAuth2SuccessHandler customOAuth2SuccessHandler
  ) throws Exception {

    http.csrf(AbstractHttpConfigurer::disable);
    http.formLogin(AbstractHttpConfigurer::disable);
    http.httpBasic(AbstractHttpConfigurer::disable);
    http.exceptionHandling((exceptionHandling) -> exceptionHandling
        .accessDeniedHandler(customAccessDeniedHandler())
        .authenticationEntryPoint(customAuthenticationEntryPoint())
    );

    http.cors((cors) -> cors
        .configurationSource(this::corsConfiguration)
    );

    CustomLoginFilter customLoginFilter = new CustomLoginFilter(
        authenticationManager(authenticationConfiguration),
        jwtService,
        userMapper
    );
    customLoginFilter.setFilterProcessesUrl("/auth/login");
    http.addFilterAt(customLoginFilter, UsernamePasswordAuthenticationFilter.class);

    CustomLogoutFilter customLogoutFilter = new CustomLogoutFilter(
        refreshTokenRepository,
        jwtService,
        cookieUtil
    );
    http.addFilterBefore(customLogoutFilter, LogoutFilter.class);

    JWTFilter jwtFilter = new JWTFilter(jwtUtil, userMapper, userDetailsService);
    http.addFilterBefore(jwtFilter, CustomLoginFilter.class);

    http.oauth2Login((oauth2) -> oauth2
        .userInfoEndpoint(userInfoEndpointConfig -> userInfoEndpointConfig
            .userService(customOAuth2UserService))
        .successHandler(customOAuth2SuccessHandler)
    );

    http.sessionManagement((auth) -> auth
        .maximumSessions(1)
        .maxSessionsPreventsLogin(true)
    );

    return http.build();
  }

  private CorsConfiguration corsConfiguration(HttpServletRequest request) {

    CorsConfiguration corsConfiguration = new CorsConfiguration();
    corsConfiguration.setAllowedOrigins(Collections.singletonList(frontEndUrl));
    corsConfiguration.setAllowedMethods(Collections.singletonList("*"));
    corsConfiguration.setAllowCredentials(true);
    corsConfiguration.setAllowedHeaders(Collections.singletonList("*"));
    corsConfiguration.setMaxAge(60 * 60L);
    corsConfiguration.setExposedHeaders(Collections.singletonList("Authorization"));

    return corsConfiguration;
  }
}
