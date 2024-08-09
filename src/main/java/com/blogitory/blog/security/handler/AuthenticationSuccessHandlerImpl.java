package com.blogitory.blog.security.handler;

import static com.blogitory.blog.security.util.JwtUtils.ACCESS_COOKIE_EXPIRE;
import static com.blogitory.blog.security.util.JwtUtils.ACCESS_TOKEN_COOKIE_NAME;
import static com.blogitory.blog.security.util.JwtUtils.makeSecureCookie;

import com.blogitory.blog.jwt.service.JwtService;
import com.blogitory.blog.member.dto.response.LoginMemberResponseDto;
import com.blogitory.blog.security.users.UserDetailsImpl;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

/**
 * Implementation of AuthenticationSuccessHandler.
 *
 * @author woonseok
 * @since 1.0
 **/
@Slf4j
@RequiredArgsConstructor
public class AuthenticationSuccessHandlerImpl implements AuthenticationSuccessHandler {
  private final JwtService jwtService;

  @Override
  public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                      Authentication authentication) {
    String uuid = UUID.randomUUID().toString();
    UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getDetails();
    LoginMemberResponseDto loginMemberResponseDto =
            new LoginMemberResponseDto(
                    userDetails.getUserNo(),
                    userDetails.getUsername(),
                    userDetails.getIdName(),
                    userDetails.getName(),
                    userDetails.getPfp(),
                    userDetails.getPassword(),
                    authentication.getAuthorities()
                            .stream().map(Object::toString)
                            .toList());

    String accessToken = jwtService.issue(uuid, loginMemberResponseDto);

    ResponseCookie cookie =
            makeSecureCookie(ACCESS_TOKEN_COOKIE_NAME, accessToken, ACCESS_COOKIE_EXPIRE);

    response.addHeader("Set-Cookie", cookie.toString());
  }
}
