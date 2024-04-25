package com.blogitory.blog.jwt.provider;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import io.jsonwebtoken.Claims;
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
 **/
class JwtProviderTest {
  JwtProvider jwtProvider;

  @BeforeEach
  void setUp() {
    jwtProvider = new JwtProvider();
  }

  @Test
  @DisplayName("토큰 생성")
  void createToken() {
    String secret = "f639aebf4fe7ab4f8bc8fd1e545cc9e0a968fa2c9e125c99b8a0b04deeab54eb6ba5c1a9c09b09c263d145eaffa6011809352efa3945c869af7f8944ca5df342";
    String value = "value";
    List<String> roles = List.of("ROLE_DUMMY");
    Long expire = 1000L;

    String expect = "eyJhbGciOiJIUzUxMiJ9";
    String result = jwtProvider.createToken(secret, value, roles, expire);

    assertThat(result).contains(expect);
  }

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

  @Test
  @DisplayName("클레임 get")
  void getClaims() {
    String secret = "f639aebf4fe7ab4f8bc8fd1e545cc9e0a968fa2c9e125c99b8a0b04deeab54eb6ba5c1a9c09b09c263d145eaffa6011809352efa3945c869af7f8944ca5df342";
    String value = "value";
    String testToken = jwtProvider.createToken(secret, value, List.of(), 100000L);

    Claims claims = jwtProvider.getClaims(testToken, secret);
    String result = claims.getSubject();

    assertThat(result).isEqualTo(value);
  }

  @Test
  @DisplayName("토큰 만료되지 않음")
  void isExpired() {
    String secret = "f639aebf4fe7ab4f8bc8fd1e545cc9e0a968fa2c9e125c99b8a0b04deeab54eb6ba5c1a9c09b09c263d145eaffa6011809352efa3945c869af7f8944ca5df342";
    String value = "value";
    String testToken = jwtProvider.createToken(secret, value, List.of(), 100000L);

    assertFalse(jwtProvider.isExpired(testToken, secret));
  }

  @Test
  @DisplayName("토큰 만료됨")
  void isExpiredFalse() {
    String secret = "f639aebf4fe7ab4f8bc8fd1e545cc9e0a968fa2c9e125c99b8a0b04deeab54eb6ba5c1a9c09b09c263d145eaffa6011809352efa3945c869af7f8944ca5df342";
    String value = "value";
    String testToken = jwtProvider.createToken(secret, value, List.of(), 0L);

    assertTrue(jwtProvider.isExpired(testToken, secret));
  }
}