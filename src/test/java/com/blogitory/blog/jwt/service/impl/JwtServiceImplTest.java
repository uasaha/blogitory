package com.blogitory.blog.jwt.service.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.blogitory.blog.jwt.dto.MemberInfoDto;
import com.blogitory.blog.jwt.properties.JwtProperties;
import com.blogitory.blog.jwt.provider.JwtProvider;
import com.blogitory.blog.jwt.service.JwtService;
import com.blogitory.blog.member.dto.MemberLoginResponseDto;
import com.blogitory.blog.security.exception.AuthenticationException;
import com.blogitory.blog.security.exception.AuthorizationException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.impl.DefaultClaims;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

/**
 * JWT Service Impl test.
 *
 * @author woonseok
 * @since 1.0
 */
class JwtServiceImplTest {
  /**
   * The Jwt provider.
   */
  JwtProvider jwtProvider;
  /**
   * The Jwt properties.
   */
  JwtProperties jwtProperties;
  /**
   * The Redis template.
   */
  RedisTemplate<String, Object> redisTemplate;
  /**
   * The Object mapper.
   */
  ObjectMapper objectMapper;
  /**
   * The Jwt service.
   */
  JwtService jwtService;

  /**
   * Sets up.
   */
  @BeforeEach
  void setUp() {
    jwtProvider = Mockito.mock(JwtProvider.class);
    jwtProperties = Mockito.mock(JwtProperties.class);
    redisTemplate = Mockito.mock(RedisTemplate.class);
    objectMapper = Mockito.mock(ObjectMapper.class);

    jwtService = new JwtServiceImpl(jwtProvider, jwtProperties, redisTemplate, objectMapper);
  }

  /**
   * Issue.
   *
   * @throws Exception the exception
   */
  @Test
  @DisplayName("발급 성공")
  void issue() throws Exception {
    String secret = "secret";
    Duration expire = Duration.of(600, ChronoUnit.SECONDS);
    MemberLoginResponseDto memberLoginResponseDto = new MemberLoginResponseDto(
            1, "test@email.com", "username", "name", "password", List.of("ROLE_TEST"));

    String expect = "accessToken";
    String uuid = UUID.randomUUID().toString();
    List<String> roles = List.of("ROLE_TEST");

    ValueOperations<String, Object> operations = mock(ValueOperations.class);
    String info = "info";

    when(jwtProperties.getRefreshSecret()).thenReturn(secret);
    when(jwtProperties.getRefreshExpire()).thenReturn(expire);
    when(jwtProvider.createToken(any(), anyString(), anyLong())).thenReturn(expect);
    when(jwtProperties.getAccessSecret()).thenReturn(secret);
    when(jwtProperties.getAccessExpire()).thenReturn(expire);
    doNothing().when(operations).set(any(), any());
    when(redisTemplate.opsForValue()).thenReturn(operations);
    when(redisTemplate.expire(uuid, 14, TimeUnit.DAYS)).thenReturn(true);
    when(objectMapper.writeValueAsString(any())).thenReturn(info);

    String result = jwtService.issue(uuid, memberLoginResponseDto);

    assertEquals(expect, result);
  }

  /**
   * Issue failed.
   *
   * @throws Exception the exception
   */
  @Test
  @DisplayName("발급 실패")
  void issueFailed() throws Exception {
    String secret = "secret";
    Duration expire = Duration.of(600, ChronoUnit.SECONDS);
    MemberLoginResponseDto memberLoginResponseDto = new MemberLoginResponseDto(
            1, "test@email.com", "username", "name", "password", List.of("ROLE_TEST"));

    String expect = "accessToken";
    String uuid = UUID.randomUUID().toString();
    List<String> roles = List.of("ROLE_TEST");

    ValueOperations<String, Object> operations = mock(ValueOperations.class);

    when(jwtProperties.getRefreshSecret()).thenReturn(secret);
    when(jwtProperties.getRefreshExpire()).thenReturn(expire);
    when(jwtProvider.createToken(any(), anyString(), anyLong())).thenReturn(expect);
    when(jwtProperties.getAccessSecret()).thenReturn(secret);
    when(jwtProperties.getAccessExpire()).thenReturn(expire);
    doNothing().when(operations).set(any(), any());
    when(redisTemplate.opsForValue()).thenReturn(operations);
    when(redisTemplate.expire(uuid, 14, TimeUnit.DAYS)).thenReturn(true);
    when(objectMapper.writeValueAsString(any())).thenThrow(JsonProcessingException.class);

    assertThrows(AuthenticationException.class, () -> jwtService.issue(uuid, memberLoginResponseDto));
  }

  /**
   * Re issue.
   *
   * @throws Exception the exception
   */
  @Test
  @DisplayName("재발급 성공")
  void reIssue() throws Exception {
    String uuid = UUID.randomUUID().toString();
    MemberInfoDto info = new MemberInfoDto(
            1, "test@email.com", "username", "name", List.of("ROLE_TEST"), "refreshToken");
    Claims claims = new DefaultClaims(Map.of());
    String accessToken = "accessToken";

    ValueOperations<String, Object> operations = mock(ValueOperations.class);

    when(redisTemplate.opsForValue()).thenReturn(operations);
    when(operations.getAndDelete(any())).thenReturn(info.toString());
    when(objectMapper.readValue(anyString(), (Class<Object>) any())).thenReturn(info);
    when(jwtProvider.isExpired(any(), any())).thenReturn(false);
    when(jwtProperties.getRefreshSecret()).thenReturn("secret");
    when(jwtProvider.getClaims(any(), any())).thenReturn(claims);
    when(jwtProvider.createToken(any(), anyString(), anyLong())).thenReturn(accessToken);

    Map<String, String> result = jwtService.reIssue(uuid);

    assertEquals(accessToken, result.get("accessToken"));
    assertEquals(1, info.getMemberNo());
    assertEquals("test@email.com", info.getEmail());
    assertEquals("username", info.getUsername());
    assertEquals("name", info.getName());
    assertEquals("refreshToken", info.getRefreshToken());
  }

  /**
   * Re issue failed.
   *
   * @throws Exception AuthenticationException
   */
  @Test
  @DisplayName("재발급 실패 - redis get null")
  void reIssueFailed() throws Exception {
    String uuid = UUID.randomUUID().toString();

    Claims claims = new DefaultClaims(Map.of());
    String accessToken = "accessToken";

    ValueOperations<String, Object> operations = mock(ValueOperations.class);

    when(redisTemplate.opsForValue()).thenReturn(operations);
    when(operations.getAndDelete(any())).thenReturn(null);
    when(objectMapper.readValue(anyString(), (Class<Object>) any())).thenReturn(null);
    when(jwtProvider.isExpired(any(), any())).thenReturn(false);
    when(jwtProperties.getRefreshSecret()).thenReturn("secret");
    when(jwtProvider.getClaims(any(), any())).thenReturn(claims);
    when(jwtProvider.createToken(any(), anyString(), anyLong())).thenReturn(accessToken);

    assertThrows(AuthenticationException.class, () -> jwtService.reIssue(uuid));
  }

  /**
   * Re issue Failed.
   *
   * @throws Exception IllegalArgumentException
   */
  @Test
  @DisplayName("재발급 실패 - object mapper")
  void reIssueFailedObjectMapper() throws Exception {
    String uuid = UUID.randomUUID().toString();
    MemberInfoDto info = new MemberInfoDto(
            1, "test@email.com", "username", "name", List.of("ROLE_TEST"), "refreshToken");
    Claims claims = new DefaultClaims(Map.of());
    String accessToken = "accessToken";

    ValueOperations<String, Object> operations = mock(ValueOperations.class);

    when(redisTemplate.opsForValue()).thenReturn(operations);
    when(operations.getAndDelete(any())).thenReturn(info.toString());
    when(objectMapper.readValue(anyString(), (Class<Object>) any())).thenThrow(IllegalArgumentException.class);
    when(jwtProvider.isExpired(any(), any())).thenReturn(false);
    when(jwtProperties.getRefreshSecret()).thenReturn("secret");
    when(jwtProvider.getClaims(any(), any())).thenReturn(claims);
    when(jwtProvider.createToken(any(), anyString(), anyLong())).thenReturn(accessToken);

    assertThrows(AuthenticationException.class, () -> jwtService.reIssue(uuid));

  }
}