package com.ssafyhome.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssafyhome.auth.handler.CustomOAuth2SuccessHandler;
import com.ssafyhome.auth.filter.CustomLoginFilter;
import com.ssafyhome.auth.filter.CustomLogoutFilter;
import com.ssafyhome.auth.filter.JWTFilter;
import com.ssafyhome.user.dao.UserMapper;
import com.ssafyhome.auth.dao.RefreshTokenRepository;
import com.ssafyhome.auth.service.JWTService;
import com.ssafyhome.auth.service.CustomOAuth2UserService;
import com.ssafyhome.auth.service.CustomUserDetailsService;
import com.ssafyhome.common.util.CookieUtil;
import com.ssafyhome.common.util.ErrorUtil;
import com.ssafyhome.common.util.JWTUtil;
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
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.web.cors.CorsConfiguration;

import java.util.Collections;

/**
 * 나야 스프링 시큐리티
 *
 * Spring Security와 관련한 설정 - 이게 러닝커브지 음음
 * @EnableMethodSecurity - FilterChain에서 각 URL별 권한 설정할 수도 있는데 우리는 @PreAuthorize을 통해서 각 메서드에 접근 권한을 설정
 */
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

  /**
   * 비밀번호 등 암호화를 위한 빈 생성
   */
  @Bean
  public BCryptPasswordEncoder passwordEncoder() {

    return new BCryptPasswordEncoder();
  }

  /**
   * Role 사이의 관계를 설정
   * 부등호 등으로 직관적으로 Role사이의 관계 설정이 가능
   * 부등호로 나타내면 하위 Role의 권한을 모두 획득
   */
  @Bean
  public RoleHierarchy roleHierarchy() {

    String hierarchy = "ROLE_ADMIN > ROLE_MANAGER > ROLE_USER";
    return RoleHierarchyImpl.fromHierarchy(hierarchy);
  }

  /**
   * @PreAuthorize의 경우 Filter단에서 권한을 처리하므로 ControllerAdvice를 통한 Handling에 어려움이 있음
   * 그래서 따로 인증 및 권한 오류를 핸들링 하기 위해 빈으로 등록
   * - customAccessDeniedHandler - 403 Forbidden
   * - customAuthenticationEntryPoint - 401 Unauthorized
   */
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

  
  /**
   * Spring Security에서의 Cors권한 설정
   */
  private CorsConfiguration corsConfiguration(HttpServletRequest request) {

    CorsConfiguration corsConfiguration = new CorsConfiguration();
    corsConfiguration.setAllowedOrigins(Collections.singletonList(frontEndUrl));
    corsConfiguration.setAllowedMethods(Collections.singletonList("*"));
    corsConfiguration.setAllowCredentials(true); // 쿠키 등의 자격증명 전송을 허용
    corsConfiguration.setAllowedHeaders(Collections.singletonList("*"));
    corsConfiguration.setMaxAge(60 * 60L);
    corsConfiguration.setExposedHeaders(Collections.singletonList("Authorization"));

    return corsConfiguration;
  }

  /**
   * Spring Security의 존재의의
   *
   * Spring Application 접근 전 필터단을 거치다가 DelegatingFilterProxy의 FilterChainProxy를 거치면서 작동
   * 해당 Proxy를 거치면서 SpringApplication 내부에 있는 SecurityFilterChain을 미리 접근하여 수행하며 작동
   */
  @Bean
  public SecurityFilterChain securityFilterChain(
      HttpSecurity http,
      CustomOAuth2UserService customOAuth2UserService,
      CustomOAuth2SuccessHandler customOAuth2SuccessHandler
  ) throws Exception {

    /**
     * csrf, fromLogin, httpBasicLogin Disable
     * Rest API의 경우 Stateless하고 JWT를 사용하므로 csrf공격이 유효하지 않으므로 disable
     * Vue.js를 통한 FE에서 로그인 페이지를 별도로 구현하므로 기본 제공 로그인 방식을 막음
     */
    http.csrf(AbstractHttpConfigurer::disable); // 사용자 요청 위조 (Cross Site Request Forgery)
    http.formLogin(AbstractHttpConfigurer::disable);
    http.httpBasic(AbstractHttpConfigurer::disable);
    /**
     * 메서드 접근 불가시 종류에 따라서 custom bean에 따라 로직 수행
     */
    http.exceptionHandling((exceptionHandling) -> exceptionHandling
        .accessDeniedHandler(customAccessDeniedHandler())
        .authenticationEntryPoint(customAuthenticationEntryPoint())
    );

    /**
     * cors권한 허용
     */
    http.cors((cors) -> cors
        .configurationSource(this::corsConfiguration)
    );

    /**
     * 로그인 filter custom
     */
    CustomLoginFilter customLoginFilter = new CustomLoginFilter(
        authenticationManager(authenticationConfiguration),
        jwtService,
        userMapper
    );
    
    
    customLoginFilter.setFilterProcessesUrl("/auth/login");
    http.addFilterAt(customLoginFilter, UsernamePasswordAuthenticationFilter.class);

    /**
     * 로그아웃 filter custom
     */
    CustomLogoutFilter customLogoutFilter = new CustomLogoutFilter(
        refreshTokenRepository,
        jwtService,
        cookieUtil
    );
    http.addFilterBefore(customLogoutFilter, LogoutFilter.class);

    /**
     * JWT가 유효한지 인증을 거치는 filter추가
     */
    JWTFilter jwtFilter = new JWTFilter(jwtUtil, userMapper, userDetailsService);
    http.addFilterBefore(jwtFilter, CustomLoginFilter.class);

    
    /**
     * OAtuh2 관련 설정
     */
    http.oauth2Login((oauth2) -> oauth2
        .userInfoEndpoint(userInfoEndpointConfig -> userInfoEndpointConfig
            .userService(customOAuth2UserService))
        .successHandler(customOAuth2SuccessHandler)
    );

    /**
     * JWT를 사용하면서 세션을 통한 로그인을 하지 않으므로 세션의 생성을 하지 않도록 설정
     */
    http.sessionManagement((session) -> session
        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
    );

    return http.build();
  }
}
