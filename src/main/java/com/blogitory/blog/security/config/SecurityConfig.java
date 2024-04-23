package com.blogitory.blog.security.config;

import com.blogitory.blog.jwt.properties.JwtProperties;
import com.blogitory.blog.jwt.service.JwtService;
import com.blogitory.blog.member.service.MemberService;
import com.blogitory.blog.security.filter.AuthenticationFailureHandlerFilter;
import com.blogitory.blog.security.filter.AuthenticationFilterCustom;
import com.blogitory.blog.security.filter.UsernamePasswordAuthenticationFilterCustom;
import com.blogitory.blog.security.handler.AuthenticationSuccessHandlerImpl;
import com.blogitory.blog.security.handler.LogoutHandlerImpl;
import com.blogitory.blog.security.handler.LogoutSuccessHandlerImpl;
import com.blogitory.blog.security.provider.AuthenticationProviderImpl;
import com.blogitory.blog.security.service.UserDetailsServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

/**
 * Config class for spring security.
 *
 * @author woonseok
 * @since 1.0
 **/
@Configuration
@RequiredArgsConstructor
public class SecurityConfig {
  private final UserDetailsServiceImpl userDetailsService;
  private final MemberService memberService;
  private final JwtProperties jwtProperties;
  private final RedisTemplate<String, Object> redisTemplate;
  private final ObjectMapper objectMapper;
  private final JwtService jwtService;

  /**
   * Register SecurityFilterChain bean.
   *
   * @param http HttpSecurity
   * @return SecurityFilterChain instance
   * @throws Exception exception
   */
  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    AuthenticationManager authenticationManager
            = authenticationManager(http.getSharedObject(AuthenticationConfiguration.class));

    return http.csrf(AbstractHttpConfigurer::disable)
            .cors(AbstractHttpConfigurer::disable)
            .formLogin(login -> login
                    .successHandler(authenticationSuccessHandler())
                    .disable())
            .logout(logout ->
                    logout.addLogoutHandler(logoutHandler())
                            .logoutSuccessHandler(logoutSuccessHandler()))
            .httpBasic(AbstractHttpConfigurer::disable)
            .sessionManagement(configurer ->
                    configurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(registry ->
                    registry.requestMatchers("/admin/**").hasAnyAuthority("ROLE_ADMIN")
                            .requestMatchers("/logout").authenticated()
                            .anyRequest().permitAll())
            .addFilterAt(usernamePasswordAuthenticationFilterCustom(
                            authenticationManager, memberService),
                    UsernamePasswordAuthenticationFilter.class)
            .addFilterBefore(authenticationFilterCustom(),
                    UsernamePasswordAuthenticationFilter.class)
            .addFilterBefore(authenticationFailureHandlerFilter(),
                    UsernamePasswordAuthenticationFilter.class)
            .build();
  }

  /**
   * Register AuthenticationFailureHandlerFilter bean.
   *
   * @return AuthenticationFailureHandlerFilter
   */
  @Bean
  public AuthenticationFailureHandlerFilter authenticationFailureHandlerFilter() {
    return new AuthenticationFailureHandlerFilter();
  }

  /**
   * Register LogoutSuccessHandler bean.
   *
   * @return LogoutSuccessHandler
   */
  @Bean
  public LogoutSuccessHandler logoutSuccessHandler() {
    return new LogoutSuccessHandlerImpl();
  }

  /**
   * Register LogoutHandler bean.
   *
   * @return LogoutHandler
   */
  @Bean
  public LogoutHandler logoutHandler() {
    return new LogoutHandlerImpl(redisTemplate);
  }

  /**
   * Register AuthenticationSuccessHandler bean.
   *
   * @return AuthenticationSuccessHandler
   */
  @Bean
  public AuthenticationSuccessHandler authenticationSuccessHandler() {
    return new AuthenticationSuccessHandlerImpl();
  }

  /**
   * Register AuthenticationFilterCustom bean.
   *
   * @return AuthenticationFilterCustom
   */
  @Bean
  public AuthenticationFilterCustom authenticationFilterCustom() {
    return new AuthenticationFilterCustom(jwtProperties,
            redisTemplate,
            objectMapper,
            jwtService);
  }

  /**
   * Register UsernamePasswordAuthenticationFilterCustom bean.
   *
   * @param authenticationManager AuthenticationManager
   * @param memberService         MemberService
   * @return UsernamePasswordAuthenticationFilterCustom
   */
  @Bean
  public UsernamePasswordAuthenticationFilterCustom
      usernamePasswordAuthenticationFilterCustom(
          AuthenticationManager authenticationManager, MemberService memberService) {
    UsernamePasswordAuthenticationFilterCustom loginFilter =
            new UsernamePasswordAuthenticationFilterCustom(memberService);

    loginFilter.setFilterProcessesUrl("/login");
    loginFilter.setAuthenticationManager(authenticationManager);
    loginFilter.setUsernameParameter("id");
    loginFilter.setPasswordParameter("pwd");
    loginFilter.setAuthenticationSuccessHandler(authenticationSuccessHandler());

    return loginFilter;
  }

  /**
   * Register AuthenticationManager bean.
   *
   * @param authenticationConfiguration AuthenticationConfiguration
   * @return AuthenticationManager
   * @throws Exception exception
   */
  @Bean
  public AuthenticationManager authenticationManager(
          AuthenticationConfiguration authenticationConfiguration) throws Exception {
    return authenticationConfiguration.getAuthenticationManager();
  }

  /**
   * Register AuthenticationProvider bean.
   *
   * @return AuthenticationProvider
   */
  @Bean
  public AuthenticationProvider authenticationProvider() {

    return new AuthenticationProviderImpl(userDetailsService);
  }

}
