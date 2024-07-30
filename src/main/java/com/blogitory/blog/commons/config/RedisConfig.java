package com.blogitory.blog.commons.config;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * Config class for Redis.
 *
 * @author woonseok
 * @since 1.0
 **/
@Configuration
@RequiredArgsConstructor
public class RedisConfig {
  private final RedisProperties redisProperties;

  /**
   * Setting RedisConnectionFactory as LettuceConnectionFactory.
   *
   * @return LettuceConnectionFactory
   */
  @Bean
  public RedisConnectionFactory redisConnectionFactory() {
    final LettuceClientConfiguration clientConfiguration =
            LettuceClientConfiguration.builder()
                    .useSsl().build();

    RedisStandaloneConfiguration standaloneConfiguration =
            new RedisStandaloneConfiguration();

    standaloneConfiguration.setHostName(redisProperties.getHost());
    standaloneConfiguration.setPort(redisProperties.getPort());
    standaloneConfiguration.setPassword(redisProperties.getPassword());

    return new LettuceConnectionFactory(standaloneConfiguration, clientConfiguration);
  }

  /**
   * Setting RedisTemplate bean.
   *
   * @return RedisTemplate
   */
  @Bean
  public RedisTemplate<String, Object> redisTemplate() {
    RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
    redisTemplate.setConnectionFactory(redisConnectionFactory());

    redisTemplate.setKeySerializer(new StringRedisSerializer());
    redisTemplate.setValueSerializer(new StringRedisSerializer());

    return redisTemplate;
  }
}
