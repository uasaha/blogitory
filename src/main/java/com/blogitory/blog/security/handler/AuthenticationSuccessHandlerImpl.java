package com.blogitory.blog.security.handler;

import static com.blogitory.blog.security.util.JwtUtils.ACCESS_COOKIE_EXPIRE;
import static com.blogitory.blog.security.util.JwtUtils.ACCESS_TOKEN_COOKIE_NAME;
import static com.blogitory.blog.security.util.JwtUtils.makeSecureCookie;

import com.blogitory.blog.jwt.service.JwtService;
import com.blogitory.blog.member.dto.MemberLoginResponseDto;
import com.blogitory.blog.security.users.UserDetailsImpl;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
    MemberLoginResponseDto memberLoginResponseDto =
            new MemberLoginResponseDto(
                    userDetails.getUserNo(),
                    userDetails.getUsername(),
                    userDetails.getIdName(),
                    userDetails.getName(),
                    userDetails.getPassword());
    List<String> roles = authentication.getAuthorities()
            .stream().map(Object::toString)
            .toList();

    String accessToken = jwtService.issue(uuid, memberLoginResponseDto, roles);
    response.addCookie(
            makeSecureCookie(ACCESS_TOKEN_COOKIE_NAME, accessToken, ACCESS_COOKIE_EXPIRE));
  }
}
