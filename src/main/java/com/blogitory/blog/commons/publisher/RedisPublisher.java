package com.blogitory.blog.commons.publisher;

import com.blogitory.blog.commons.listener.event.SseSendEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

/**
 * Redis publisher.
 *
 * @author woonseok
 * @since 1.0
 **/
@Slf4j
@Component
@RequiredArgsConstructor
public class RedisPublisher {
  private final RedisTemplate<String, Object> redisTemplate;
  private final ObjectMapper objectMapper;

  /**
   * Publish message to channel.
   *
   * @param topic   topic
   * @param message message
   */
  public void publish(String topic, String message) {
    redisTemplate.convertAndSend(topic, message);
  }

  /**
   * Publish sse event to channel.
   *
   * @param topic topic
   * @param event event
   */
  public void publish(String topic, SseSendEvent event) {
    try {
      String message = objectMapper.writeValueAsString(event);
      publish(topic, message);
    } catch (IOException e) {
      log.error(e.getMessage(), e);
    }
  }
}
