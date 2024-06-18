package com.blogitory.blog.commons.properties;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.blogitory.blog.jwt.properties.JwtProperties;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

/**
 * Properties test.
 *
 * @author woonseok
 * @since 1.0
 */
@SpringBootTest
@ActiveProfiles("test")
class PropertiesTest {
  /**
   * The Object storage properties.
   */
  @Autowired
  ObjectStorageProperties objectStorageProperties;

  /**
   * The Redis properties.
   */
  @Autowired
  RedisProperties redisProperties;

  /**
   * The Jwt properties.
   */
  @Autowired
  JwtProperties jwtProperties;

  /**
   * Object storage properties test.
   */
  @Test
  void objectStoragePropertiesTest() {
    assertAll(
            () -> assertEquals("accessKey", objectStorageProperties.getAccessKey()),
            () -> assertEquals("secretKey", objectStorageProperties.getPrivateKey()),
            () -> assertEquals("region", objectStorageProperties.getRegion()),
            () -> assertEquals("bucket", objectStorageProperties.getBucket()),
            () -> assertEquals("bucket-url", objectStorageProperties.getReturnUrl()));
  }

  /**
   * Redis properties test.
   */
  @Test
  void redisPropertiesTest() {
    assertAll(
            () -> assertEquals("localhost", redisProperties.getHost()),
            () -> assertEquals("6379", redisProperties.getPort()));
  }

  /**
   * Jwt properties test.
   */
  @Test
  void jwtPropertiesTest() {
    assertAll(
            () -> assertEquals("9ed8627f25c4f500b1b2a5f3c7ad2527d0d6a5ecee660890835c8b67a010359e2e0a083e4aefece8e47983285a29903d5e392c912c51252e9e2f5291590e0d0a",
                    jwtProperties.getAccessSecret()),
            () -> assertEquals(1800000L, jwtProperties.getAccessExpire().toMillis()),
            () -> assertEquals("7467d18eb661986301956823c6a44194211e31b70de44f14d410c412dd6a21d56a14ef0fcdae8391c7cdeb6d5b5fa2051118575ce343834a4a2755afa7d1f3f8",
                    jwtProperties.getRefreshSecret()),
            () -> assertEquals(1209600000L, jwtProperties.getRefreshExpire().toMillis())
    );
  }
}