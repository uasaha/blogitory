package com.blogitory.blog.commons.config;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.blogitory.blog.commons.listener.RedisMessageListener;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * RedisConfigTest.
 *
 * @author woonseok
 * @since 1.0
 **/
class RedisConfigTest {
  RedisConfig redisConfig;
  RedisProperties redisProperties;
  RedisMessageListener redisMessageListener;

  @BeforeEach
  void setUp() {
    redisProperties = mock(RedisProperties.class);
    redisMessageListener = mock(RedisMessageListener.class);
    redisConfig = new RedisConfig(redisProperties, redisMessageListener);
  }

  @Test
  void redisConnectionFactory() {
    when(redisProperties.getHost()).thenReturn("localhost");
    when(redisProperties.getPort()).thenReturn(6379);
    when(redisProperties.getPassword()).thenReturn("password");

    RedisConnectionFactory connectionFactory = redisConfig.redisConnectionFactory();

    assertInstanceOf(LettuceConnectionFactory.class, connectionFactory);
  }

  @Test
  void redisTemplate() {
    when(redisProperties.getHost()).thenReturn("localhost");
    when(redisProperties.getPort()).thenReturn(6379);
    when(redisProperties.getPassword()).thenReturn("password");

    RedisTemplate<String, Object> redisTemplate = redisConfig.redisTemplate();
    assertInstanceOf(RedisTemplate.class, redisTemplate);
    assertInstanceOf(StringRedisSerializer.class, redisTemplate.getKeySerializer());
  }

  @Test
  void noticeTopic() {
    ChannelTopic channelTopic = redisConfig.noticeTopic();

    assertInstanceOf(ChannelTopic.class, channelTopic);
  }

  @Test
  void redisMessageListenerContainer() {
    RedisMessageListenerContainer container = redisConfig.redisMessageListenerContainer();

    when(redisProperties.getHost()).thenReturn("localhost");
    when(redisProperties.getPort()).thenReturn(6379);
    when(redisProperties.getPassword()).thenReturn("password");

    assertInstanceOf(RedisMessageListenerContainer.class, container);
  }
}