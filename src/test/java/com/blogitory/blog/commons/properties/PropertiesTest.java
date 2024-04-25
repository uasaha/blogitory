package com.blogitory.blog.commons.properties;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

/**
 * 설명 작성 필!
 *
 * @author woonseok
 * @since 1.0
 **/
@SpringBootTest
@ActiveProfiles("test")
class PropertiesTest {
  @Autowired
  ObjectStorageProperties objectStorageProperties;

  @Autowired
  RedisProperties redisProperties;

  @Test
  void objectStoragePropertiesTest() {
    assertAll(
            () -> assertEquals("accessKey", objectStorageProperties.getAccessKey()),
            () -> assertEquals("secretKey", objectStorageProperties.getPrivateKey()),
            () -> assertEquals("region", objectStorageProperties.getRegion()),
            () -> assertEquals("bucket", objectStorageProperties.getBucket()),
            () -> assertEquals("bucket-url", objectStorageProperties.getReturnUrl()));
  }

  @Test
  void redisPropertiesTest() {
    assertAll(
            () -> assertEquals("localhost", redisProperties.getHost()),
            () -> assertEquals("6379", redisProperties.getPort()));
  }

}