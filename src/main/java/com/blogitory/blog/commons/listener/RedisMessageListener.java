package com.blogitory.blog.commons.listener;

import static com.blogitory.blog.commons.config.RedisConfig.NOTIFICATION_CHANNEL;

import com.blogitory.blog.commons.listener.event.SseSendEvent;
import com.blogitory.blog.sse.exception.SseConnectionException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

/**
 * Redis message listener.
 *
 * @author woonseok
 * @since 1.0
 **/
@Slf4j
@RequiredArgsConstructor
@Component
public class RedisMessageListener implements MessageListener {
  private final ApplicationEventPublisher eventPublisher;
  private final ObjectMapper objectMapper;

  @Override
  public void onMessage(Message message, byte[] pattern) {
    String channel = new String(message.getChannel());

    log.debug("receive message:{}", new String(message.getBody()));

    if (NOTIFICATION_CHANNEL.equals(channel)) {
      receiveSseEvent(message.getBody());
    }
  }

  private void receiveSseEvent(byte[] body) {
    SseSendEvent event;
    try {
      event = objectMapper.readValue(body, SseSendEvent.class);
    } catch (IOException e) {
      throw new SseConnectionException();
    }

    if (Objects.nonNull(event)) {
      eventPublisher.publishEvent(event);
    }
  }
}