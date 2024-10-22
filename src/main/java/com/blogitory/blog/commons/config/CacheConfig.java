package com.blogitory.blog.commons.config;

import java.time.Duration;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * Spring cache configuration.
 *
 * @author woonseok
 * @since 1.0
 **/
@Configuration
@EnableCaching
public class CacheConfig {

  /**
   * Set redis cache configuration.
   *
   * @return redisCacheConfiguration
   */
  @Bean
  public RedisCacheConfiguration redisCacheConfiguration() {
    return RedisCacheConfiguration.defaultCacheConfig()
            .entryTtl(Duration.ofMinutes(10))
            .disableCachingNullValues()
            .serializeKeysWith(RedisSerializationContext
                    .SerializationPair.fromSerializer(new StringRedisSerializer()))
            .serializeValuesWith(RedisSerializationContext
                    .SerializationPair.fromSerializer(new GenericJackson2JsonRedisSerializer()));
  }
}
