package com.blogitory.blog.security.handler;

import static com.blogitory.blog.security.util.JwtUtils.ACCESS_TOKEN_COOKIE_NAME;
import static com.blogitory.blog.security.util.JwtUtils.BLACK_LIST_KEY;
import static com.blogitory.blog.security.util.JwtUtils.getUuid;
import static com.blogitory.blog.security.util.JwtUtils.makeSecureCookie;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Arrays;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.LogoutHandler;

/**
 * Implementation of LogoutHandler.
 *
 * @author woonseok
 * @since 1.0
 **/
@RequiredArgsConstructor
public class LogoutHandlerImpl implements LogoutHandler {
  private final RedisTemplate<String, Object> redisTemplate;

  @Override
  public void logout(HttpServletRequest request,
                     HttpServletResponse response,
                     Authentication authentication) {
    Cookie accessTokenCookie = Arrays.stream(request.getCookies())
            .filter(cookie -> ACCESS_TOKEN_COOKIE_NAME.equals(cookie.getName()))
            .findFirst()
            .orElse(null);

    if (accessTokenCookie == null) {
      return;
    }

    String accessToken = accessTokenCookie.getValue();
    String uuid = getUuid(accessToken);

    redisTemplate.opsForValue().getAndDelete(uuid);

    Cookie cookie = makeSecureCookie(ACCESS_TOKEN_COOKIE_NAME, "", 0);
    response.addCookie(cookie);

    HashOperations<String, String, String> operations = redisTemplate.opsForHash();
    operations.put(BLACK_LIST_KEY, uuid, accessToken);

    SecurityContextHolder.clearContext();
  }
}
