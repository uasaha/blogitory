package com.blogitory.blog.jwt.provider;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * JWT Provider Test.
 *
 * @author woonseok
 * @since 1.0
 */
class JwtProviderTest {
  /**
   * The Jwt provider.
   */
  JwtProvider jwtProvider;

  /**
   * Sets up.
   */
  @BeforeEach
  void setUp() {
    jwtProvider = new JwtProvider();
  }

  /**
   * Create token.
   */
  @Test
  @DisplayName("토큰 생성")
  void createToken() {
    String secret = "f639aebf4fe7ab4f8bc8fd1e545cc9e0a968fa2c9e125c99b8a0b04deeab54eb6ba5c1a9c09b09c263d145eaffa6011809352efa3945c869af7f8944ca5df342";
    String value = "value";
    Long expire = 1000L;

    String expect = "eyJhbGciOiJIUzUxMiJ9";
    String result = jwtProvider.createToken(secret, value, expire);

    assertThat(result).contains(expect);
  }

  /**
   * Testtest.
   */
  @Test
  void testtest() {
    String secret = "Ny0pm2CWIAST07ElsTAVZgCqJKJd2bE9lpKyewuOhyyKoBApt1Ny0pm2CWIAST07ElsTAVZgCqJKJd2bE9lpKyewuOhyyKoBApt1";
    String accessToken = "eyJhbGciOiJIUzI1NiJ9.eyJ1c2VySWQiOjk5OTksImlzcyI6Im5obi1hY2FkZW15LW1hcmNvIiwiaWF0IjoxNzEwNTA4MDQwLCJleHAiOjE4ODMzMDgwNDB9.1W0KJj7mBuYhNPqEwfJzSvQD4JsNY-75qDvvlPqWiwE";
    String accessToken2 = "eyJhbGciOiJIUzI1NiJ9.eyJ1c2VySWQiOjEwMCwiaXNzIjoibmhuLWFjYWRlbXktbWFyY28iLCJpYXQiOjE3MTA1MTIyMDgsImV4cCI6MTc5NjkxMjIwOH0.-sx5W47k7xEI24iTPSSFUzxOH8LNLzboDjmJlqykPkE";
    String accessToken3 = "eyJhbGciOiJIUzI1NiJ9.eyJ1c2VySWQiOjEwMCwiaXNzIjoibmhuLWFjYWRlbXktbWFyY28iLCJpYXQiOjE2ODk2ODI2OTQsImV4cCI6MTY4OTc2OTA5NH0.xqRwAteYg1u8sFBi9oGrQgUtns25UplflZgunUrN50A";

    assertNotNull(jwtProvider.getClaims(accessToken, secret));
    System.out.println(jwtProvider.getClaims(accessToken, secret));
    assertNotNull(jwtProvider.getClaims(accessToken2, secret));
    System.out.println(jwtProvider.getClaims(accessToken2, secret));
    assertThrows(ExpiredJwtException.class, () -> jwtProvider.getClaims(accessToken3, secret));
  }

  /**
   * Test create token.
   */
  @Test
  @DisplayName("토큰 생성 2")
  void testCreateToken() {
    String secret = "f639aebf4fe7ab4f8bc8fd1e545cc9e0a968fa2c9e125c99b8a0b04deeab54eb6ba5c1a9c09b09c263d145eaffa6011809352efa3945c869af7f8944ca5df342";
    List<String> roles = List.of("ROLE_DUMMY");
    Claims claims = Jwts.claims();
    claims.setSubject("value");
    claims.put("roles", roles);

    String expect = "eyJhbGciOiJIUzUxMiJ9";
    String result = jwtProvider.createToken(secret, claims, 1000L);

    assertThat(result).contains(expect);
  }

  /**
   * Gets claims.
   */
  @Test
  @DisplayName("클레임 get")
  void getClaims() {
    String secret = "f639aebf4fe7ab4f8bc8fd1e545cc9e0a968fa2c9e125c99b8a0b04deeab54eb6ba5c1a9c09b09c263d145eaffa6011809352efa3945c869af7f8944ca5df342";
    String value = "value";
    String testToken = jwtProvider.createToken(secret, value, 100000L);

    Claims claims = jwtProvider.getClaims(testToken, secret);
    String result = claims.getSubject();

    assertThat(result).isEqualTo(value);
  }

  /**
   * Is expired.
   */
  @Test
  @DisplayName("토큰 만료되지 않음")
  void isExpired() {
    String secret = "f639aebf4fe7ab4f8bc8fd1e545cc9e0a968fa2c9e125c99b8a0b04deeab54eb6ba5c1a9c09b09c263d145eaffa6011809352efa3945c869af7f8944ca5df342";
    String value = "value";
    String testToken = jwtProvider.createToken(secret, value, 100000L);

    assertFalse(jwtProvider.isExpired(testToken, secret));
  }

  /**
   * Is expired false.
   */
  @Test
  @DisplayName("토큰 만료됨")
  void isExpiredFalse() {
    String secret = "f639aebf4fe7ab4f8bc8fd1e545cc9e0a968fa2c9e125c99b8a0b04deeab54eb6ba5c1a9c09b09c263d145eaffa6011809352efa3945c869af7f8944ca5df342";
    String value = "value";
    String testToken = jwtProvider.createToken(secret, value, 0L);

    assertTrue(jwtProvider.isExpired(testToken, secret));
  }
}