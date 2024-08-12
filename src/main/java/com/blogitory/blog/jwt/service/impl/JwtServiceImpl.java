package com.blogitory.blog.jwt.service.impl;

import com.blogitory.blog.jwt.dto.GetMemberInfoDto;
import com.blogitory.blog.jwt.properties.JwtProperties;
import com.blogitory.blog.jwt.provider.JwtProvider;
import com.blogitory.blog.jwt.service.JwtService;
import com.blogitory.blog.member.dto.response.LoginMemberResponseDto;
import com.blogitory.blog.security.exception.AuthenticationException;
import com.blogitory.blog.security.exception.AuthorizationException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
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

  public static final String REISSUE_MAP_UUID = "uuid";
  public static final String REISSUE_MAP_TOKEN = "accessToken";

  /**
   * {@inheritDoc}
   */
  @Override
  public String issue(String uuid, LoginMemberResponseDto responseDto) {
    String refreshToken = jwtProvider.createToken(
            jwtProperties.getRefreshSecret(),
            uuid,
            jwtProperties.getRefreshExpire().toMillis());

    GetMemberInfoDto infoDto = new GetMemberInfoDto(
            responseDto.getMemberNo(),
            responseDto.getEmail(),
            responseDto.getUsername(),
            responseDto.getName(),
            responseDto.getPfp(),
            responseDto.getRoles(),
            refreshToken);

    String info;

    try  {
      info = objectMapper.writeValueAsString(infoDto);
    } catch (JsonProcessingException e) {
      throw new AuthenticationException("JWT Issue failed");
    }

    ValueOperations<String, Object> operations = redisTemplate.opsForValue();
    operations.set(uuid, info);
    redisTemplate.expire(uuid, 7, TimeUnit.DAYS);

    return jwtProvider.createToken(
            jwtProperties.getAccessSecret(),
            uuid,
            jwtProperties.getAccessExpire().toMillis());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Map<String, String> reIssue(String uuid) throws JsonProcessingException {
    String info = (String) redisTemplate.opsForValue().getAndDelete(uuid);

    if (info == null) {
      throw new AuthenticationException("JWT reissue failed");
    }

    GetMemberInfoDto infoDto;
    try {
      infoDto = objectMapper.readValue(
              info, GetMemberInfoDto.class);
    } catch (IllegalArgumentException e) {
      throw new AuthenticationException("JWT reissue failed");
    }

    if (infoDto == null
            || jwtProvider.isExpired(infoDto.getRefreshToken(), jwtProperties.getRefreshSecret())) {
      throw new AuthorizationException();
    }

    String reUuid = UUID.randomUUID().toString();

    ValueOperations<String, Object> operations = redisTemplate.opsForValue();
    operations.set(reUuid, info);
    redisTemplate.expire(reUuid, 7, TimeUnit.DAYS);

    String accessToken = jwtProvider.createToken(
            jwtProperties.getAccessSecret(),
            reUuid,
            jwtProperties.getAccessExpire().toMillis());

    Map<String, String> result = new HashMap<>();
    result.put(REISSUE_MAP_UUID, reUuid);
    result.put(REISSUE_MAP_TOKEN, accessToken);

    return result;
  }
}
