package com.blogitory.blog.security.filter;

import static com.blogitory.blog.security.util.JwtUtils.ACCESS_COOKIE_EXPIRE;
import static com.blogitory.blog.security.util.JwtUtils.ACCESS_TOKEN_COOKIE_NAME;
import static com.blogitory.blog.security.util.JwtUtils.makeSecureCookie;

import com.blogitory.blog.member.dto.MemberLoginRequestDto;
import com.blogitory.blog.member.service.MemberService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Custom UsernamePasswordAuthenticationFilter.
 *
 * @author woonseok
 * @since 1.0
 **/
@Slf4j
@RequiredArgsConstructor
public class UsernamePasswordAuthenticationFilterCustom
        extends UsernamePasswordAuthenticationFilter {
  private final MemberService memberService;

  @Override
  public Authentication attemptAuthentication(HttpServletRequest request,
                                              HttpServletResponse response)
          throws AuthenticationException {
    String email = obtainUsername(request);
    String password = obtainPassword(request);

    MemberLoginRequestDto loginRequestDto = new MemberLoginRequestDto(email, password);

    String accessToken = memberService.login(loginRequestDto);

    Cookie cookie = makeSecureCookie(ACCESS_TOKEN_COOKIE_NAME, accessToken,
            ACCESS_COOKIE_EXPIRE);

    response.addCookie(cookie);

    UsernamePasswordAuthenticationToken token =
            new UsernamePasswordAuthenticationToken(email, password);

    return getAuthenticationManager().authenticate(token);
  }
}
