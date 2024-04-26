package com.blogitory.blog.jwt.service.impl;

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
import java.util.List;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementation of JwtService.
 *
 * @author woonseok
 * @since 1.0
 **/
@Service
@Transactional
@RequiredArgsConstructor
public class JwtServiceImpl implements JwtService {
  private final JwtProvider jwtProvider;
  private final JwtProperties jwtProperties;
  private final RedisTemplate<String, Object> redisTemplate;
  private final ObjectMapper objectMapper;

  /**
   * {@inheritDoc}
   */
  @Override
  public String issue(String uuid, MemberLoginResponseDto responseDto, List<String> roles) {
    String refreshToken = jwtProvider.createToken(
            jwtProperties.getRefreshSecret(),
            uuid,
            roles,
            jwtProperties.getRefreshExpire().toMillis());

    MemberInfoDto infoDto = new MemberInfoDto(
            responseDto.getMemberNo(),
            responseDto.getEmail(),
            responseDto.getName(),
            refreshToken);

    String info;

    try  {
      info = objectMapper.writeValueAsString(infoDto);
    } catch (JsonProcessingException e) {
      throw new AuthenticationException();
    }

    ValueOperations<String, Object> operations = redisTemplate.opsForValue();
    operations.set(uuid, info);
    redisTemplate.expire(uuid, 14, TimeUnit.DAYS);

    return jwtProvider.createToken(
            jwtProperties.getAccessSecret(),
            uuid,
            roles,
            jwtProperties.getAccessExpire().toMillis());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String reIssue(String uuid) throws JsonProcessingException {
    MemberInfoDto info = objectMapper.readValue(
            (String) redisTemplate.opsForValue().get(uuid), MemberInfoDto.class);

    if (info == null
            || jwtProvider.isExpired(info.getRefreshToken(), jwtProperties.getRefreshSecret())) {
      throw new AuthorizationException();
    }

    Claims claims = jwtProvider.getClaims(
            info.getRefreshToken(), jwtProperties.getRefreshSecret());

    return jwtProvider.createToken(
            jwtProperties.getAccessSecret(),
            claims,
            jwtProperties.getAccessExpire().toMillis());
  }
}
