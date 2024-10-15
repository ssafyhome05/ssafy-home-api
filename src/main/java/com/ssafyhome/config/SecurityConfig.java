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
import com.ssafyhome.util.JWTUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.Map;

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
  private final CustomUserDetailsService userDetailsService;
  private final RequestMappingHandlerMapping handlerMapping;

  public SecurityConfig(
      AuthenticationConfiguration authenticationConfiguration,
      RefreshTokenRepository refreshTokenRepository,
      UserMapper userMapper,
      JWTService jwtService,
      JWTUtil jwtUtil,
      CookieUtil cookieUtil,
      CustomUserDetailsService userDetailsService,
      RequestMappingHandlerMapping handlerMapping
  ) {

    this.authenticationConfiguration = authenticationConfiguration;
    this.refreshTokenRepository = refreshTokenRepository;
    this.userMapper = userMapper;
    this.jwtService = jwtService;
    this.jwtUtil = jwtUtil;
    this.cookieUtil = cookieUtil;
    this.userDetailsService = userDetailsService;
    this.handlerMapping = handlerMapping;
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
      String jsonResponse = mapper.writeValueAsString(getErrorDetails(request, accessDeniedException));
      response.getWriter().write(jsonResponse);
    };
  }

  @Bean
  public AuthenticationEntryPoint customAuthenticationEntryPoint() {

    return (request, response, authException) -> {
      response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
      response.setContentType("application/json;charset=UTF-8");

      ObjectMapper mapper = new ObjectMapper();
      String jsonResponse = mapper.writeValueAsString(getErrorDetails(request, authException));
      response.getWriter().write(jsonResponse);
    };
  }

  private Map<String, Object> getErrorDetails(HttpServletRequest request, Exception exception) {

    String methodName;
    String requiredAuthorities = "Unknown";

    try {
      HandlerMethod handlerMethod = (HandlerMethod) request.getAttribute(HandlerMapping.BEST_MATCHING_HANDLER_ATTRIBUTE);
      Method method = handlerMethod.getMethod();
      methodName = method.getDeclaringClass().getSimpleName() + "#" + method.getName();

      PreAuthorize preAuthorize = method.getAnnotation(PreAuthorize.class);
      if (preAuthorize != null) requiredAuthorities = preAuthorize.value();

    } catch (Exception e) {
      throw new RuntimeException(e);
    }

    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    String currentAuthorities = "No authorities";
    if (authentication != null) {
      currentAuthorities = authentication.getAuthorities().iterator().next().getAuthority();
    }

    return Map.of(
        "error", exception instanceof AccessDeniedException ? "해당 접근에 대한 권한이 필요합니다." : "인증이 필요합니다.",
        "message", exception.getMessage(),
        "method", methodName,
        "requiredAuthorities", requiredAuthorities,
        "currentAuthorities", currentAuthorities
    );
  }

  @Bean
  public SecurityFilterChain securityFilterChain(
      HttpSecurity http,
      CustomOAuth2UserService customOAuth2UserService,
      CustomOAuth2SuccessHandler customOAuth2SuccessHandler) throws Exception {

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
