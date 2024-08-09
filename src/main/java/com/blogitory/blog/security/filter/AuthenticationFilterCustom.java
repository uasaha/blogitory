package com.blogitory.blog.security.filter;

import static com.blogitory.blog.security.util.JwtUtils.ACCESS_COOKIE_EXPIRE;
import static com.blogitory.blog.security.util.JwtUtils.ACCESS_TOKEN_COOKIE_NAME;
import static com.blogitory.blog.security.util.JwtUtils.BLACK_LIST_KEY;
import static com.blogitory.blog.security.util.JwtUtils.getUuid;
import static com.blogitory.blog.security.util.JwtUtils.isExpiredToken;
import static com.blogitory.blog.security.util.JwtUtils.makeSecureCookie;
import static com.blogitory.blog.security.util.JwtUtils.needReissue;

import com.blogitory.blog.jwt.dto.GetMemberInfoDto;
import com.blogitory.blog.jwt.properties.JwtProperties;
import com.blogitory.blog.jwt.service.JwtService;
import com.blogitory.blog.security.users.UserDetailsImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseCookie;
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

  /**
   * Set Authentication with JWT.
   *
   * @param request     HttpServletRequest
   * @param response    HttpServletResponse
   * @param filterChain FilterChain
   * @throws ServletException ServletException
   * @throws IOException      IOException
   */
  @Override
  protected void doFilterInternal(@NonNull HttpServletRequest request,
                                  @NonNull HttpServletResponse response,
                                  @NonNull FilterChain filterChain)
          throws ServletException, IOException {
    if (isPassingUrl(request)) {
      filterChain.doFilter(request, response);
      return;
    }

    Cookie[] cookies = request.getCookies();

    if (cookies == null) {
      filterChain.doFilter(request, response);
      return;
    }

    Cookie accessTokenCookie = Arrays.stream(cookies)
            .filter(cookie -> ACCESS_TOKEN_COOKIE_NAME.equals(cookie.getName()))
            .findFirst()
            .orElse(null);

    if (accessTokenCookie == null
            || isExpiredToken(jwtProperties.getAccessSecret(), accessTokenCookie.getValue())) {
      filterChain.doFilter(request, response);
      return;
    }

    String accessToken = accessTokenCookie.getValue();
    String uuid = getUuid(accessToken);
    HashOperations<String, String, String> operations = redisTemplate.opsForHash();
    Optional<String> black = Optional.ofNullable(operations.get(BLACK_LIST_KEY, uuid));

    if (black.isPresent() && accessToken.equals(black.get())) {
      filterChain.doFilter(request, response);
      return;
    }

    if (needReissue(jwtProperties.getAccessSecret(), accessToken)) {
      Map<String, String> tokenMap = jwtService.reIssue(uuid);

      HashOperations<String, String, String> blackListOperation = redisTemplate.opsForHash();
      blackListOperation.put(BLACK_LIST_KEY, uuid, accessToken);

      ResponseCookie cookie = makeSecureCookie(ACCESS_TOKEN_COOKIE_NAME,
              tokenMap.get("accessToken"), ACCESS_COOKIE_EXPIRE);

      response.addHeader("Set-Cookie", cookie.toString());

      uuid = tokenMap.get("uuid");
    }

    String infoString = (String) redisTemplate.opsForValue().get(uuid);

    if (infoString == null) {
      filterChain.doFilter(request, response);
      return;
    }

    GetMemberInfoDto info = objectMapper.readValue(
            infoString, GetMemberInfoDto.class);

    List<SimpleGrantedAuthority> authorities = info.getRoles()
            .stream().map(SimpleGrantedAuthority::new).toList();

    SecurityContext context = SecurityContextHolder.getContext();

    UsernamePasswordAuthenticationToken authentication =
            new UsernamePasswordAuthenticationToken(
                    info.getMemberNo(), info.getEmail(), authorities);

    authentication.setDetails(
            new UserDetailsImpl(info.getEmail(),
                    PROTECTED,
                    info.getMemberNo(),
                    info.getUsername(),
                    info.getName(),
                    info.getPfp(),
                    authorities));

    context.setAuthentication(authentication);

    filterChain.doFilter(request, response);

    SecurityContextHolder.clearContext();
  }

  /**
   * Checking Not need url.
   *
   * @param request HttpServletRequest
   * @return is needed
   */
  private static boolean isPassingUrl(HttpServletRequest request) {
    return request.getRequestURI().contains("/static")
            || request.getRequestURI().equals("/error");
  }
}
