package com.blogitory.blog.commons.config;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;

import org.junit.jupiter.api.Test;
import org.springframework.data.redis.cache.RedisCacheConfiguration;

/**
 * CacheConfigTest.
 *
 * @author woonseok
 * @since 1.0
 **/
class CacheConfigTest {

  @Test
  void redisCacheConfiguration() {
    CacheConfig cacheConfig = new CacheConfig();
    RedisCacheConfiguration redisCacheConfiguration = cacheConfig.redisCacheConfiguration();

    assertInstanceOf(RedisCacheConfiguration.class, redisCacheConfiguration);
  }
}