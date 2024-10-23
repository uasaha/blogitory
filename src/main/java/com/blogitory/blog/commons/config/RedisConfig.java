package com.blogitory.blog.commons.config;

import com.blogitory.blog.commons.listener.RedisMessageListener;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * Config class for Redis.
 *
 * @author woonseok
 * @since 1.0
 **/
@Slf4j
@Configuration
@RequiredArgsConstructor
public class RedisConfig {
  private final RedisProperties redisProperties;
  private final RedisMessageListener messageListener;

  public static final String NOTIFICATION_CHANNEL = "notification-channel";

  /**
   * Setting RedisConnectionFactory as LettuceConnectionFactory.
   *
   * @return LettuceConnectionFactory
   */
  @Bean
  public RedisConnectionFactory redisConnectionFactory() {
    final LettuceClientConfiguration clientConfiguration =
            LettuceClientConfiguration.builder()
                    .build();

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

  /**
   * Notice Channel topic.
   *
   * @return notice channel topic
   */
  @Bean
  public ChannelTopic noticeTopic() {
    return new ChannelTopic(NOTIFICATION_CHANNEL);
  }

  /**
   * RedisMessageListenerContainer bean.
   *
   * @return RedisMessageListenerContainer
   */
  @Bean
  public RedisMessageListenerContainer redisMessageListenerContainer() {
    RedisMessageListenerContainer container = new RedisMessageListenerContainer();
    container.setConnectionFactory(redisConnectionFactory());
    container.addMessageListener(messageListener, noticeTopic());

    return container;
  }
}
