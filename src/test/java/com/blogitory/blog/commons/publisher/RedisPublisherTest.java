package com.blogitory.blog.commons.publisher;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * RedisPublisherTest.
 *
 * @author woonseok
 * @since 1.0
 **/
class RedisPublisherTest {

  RedisPublisher redisPublisher;
  RedisTemplate<String, Object> redisTemplate;
  ObjectMapper objectMapper;

  @BeforeEach
  void setUp() {
    redisTemplate = mock(RedisTemplate.class);
    objectMapper = mock(ObjectMapper.class);
    redisPublisher = new RedisPublisher(redisTemplate, objectMapper);
  }

  @Test
  void publish() {
    when(redisTemplate.convertAndSend(anyString(), anyString())).thenReturn(1L);

    redisPublisher.publish("test", "test");

    verify(redisTemplate).convertAndSend(anyString(), anyString());
  }
}