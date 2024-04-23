package com.blogitory.blog.security.filter;

import static com.blogitory.blog.security.util.JwtUtils.ACCESS_COOKIE_EXPIRE;
import static com.blogitory.blog.security.util.JwtUtils.ACCESS_TOKEN_COOKIE_NAME;
import static com.blogitory.blog.security.util.JwtUtils.BLACK_LIST_KEY;
import static com.blogitory.blog.security.util.JwtUtils.getUuid;
import static com.blogitory.blog.security.util.JwtUtils.isExpiredToken;
import static com.blogitory.blog.security.util.JwtUtils.makeSecureCookie;

import com.blogitory.blog.jwt.dto.MemberInfoDto;
import com.blogitory.blog.jwt.properties.JwtProperties;
import com.blogitory.blog.jwt.service.JwtService;
import com.blogitory.blog.security.exception.AuthenticationException;
import com.blogitory.blog.security.users.UserDetailsImpl;
import com.blogitory.blog.security.util.JwtUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * Custom AuthenticationFilter.
 *
 * @author woonseok
 * @since 1.0
 **/
@Slf4j
@RequiredArgsConstructor
public class AuthenticationFilterCustom extends OncePerRequestFilter {
  private final JwtProperties jwtProperties;
  private final RedisTemplate<String, Object> redisTemplate;
  private final ObjectMapper objectMapper;
  private final JwtService jwtService;

  private static final String PROTECTED = "[PROTECTED]";

  @Override
  protected void doFilterInternal(HttpServletRequest request,
                                  HttpServletResponse response,
                                  FilterChain filterChain) throws ServletException, IOException {
    if (isPassingUrl(request)) {
      filterChain.doFilter(request, response);
      return;
    }

    Cookie accessTokenCookie = Arrays.stream(request.getCookies())
            .filter(cookie -> ACCESS_TOKEN_COOKIE_NAME.equals(cookie.getName()))
            .findFirst()
            .orElse(null);

    if (accessTokenCookie == null) {
      filterChain.doFilter(request, response);
      return;
    }

    String accessToken = accessTokenCookie.getValue();
    String uuid = getUuid(accessToken);
    HashOperations<String, String, String> operations = redisTemplate.opsForHash();
    Optional<String> black = Optional.ofNullable(operations.get(BLACK_LIST_KEY, uuid));

    if (black.isPresent() && accessToken.equals(black.get())) {
      throw new AuthenticationException();
    }

    if (isExpiredToken(jwtProperties.getAccessSecret(), accessToken)) {
      accessToken = jwtService.reIssue(uuid);
      response.addCookie(
              makeSecureCookie(ACCESS_TOKEN_COOKIE_NAME, accessToken, ACCESS_COOKIE_EXPIRE));
    }

    MemberInfoDto info = objectMapper.readValue(
            (String) redisTemplate.opsForValue().get(uuid), MemberInfoDto.class);

    List<SimpleGrantedAuthority> authorities = JwtUtils
            .getRoles(jwtProperties.getAccessSecret(), accessToken)
            .stream().map(SimpleGrantedAuthority::new).toList();



    SecurityContext context = SecurityContextHolder.getContext();

    UsernamePasswordAuthenticationToken authentication =
            new UsernamePasswordAuthenticationToken(
                    info.getMemberNo(), info.getEmail(), authorities);

    authentication.setDetails(
            new UserDetailsImpl(info.getEmail(),
                    PROTECTED,
                    info.getMemberNo(),
                    info.getName(),
                    authorities));

    context.setAuthentication(authentication);

    filterChain.doFilter(request, response);

    SecurityContextHolder.clearContext();
  }

  private static boolean isPassingUrl(HttpServletRequest request) {
    return request.getRequestURI().contains("/static")
            || request.getRequestURI().equals("/error");
  }
}
