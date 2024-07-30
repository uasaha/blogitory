package com.blogitory.blog.security.filter;

import com.blogitory.blog.member.dto.request.MemberLoginRequestDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;

/**
 * Custom UsernamePasswordAuthenticationFilter.
 *
 * @author woonseok
 * @since 1.0
 **/
@Slf4j
public class AuthenticationProcessingFilterCustom
        extends AbstractAuthenticationProcessingFilter {

  public AuthenticationProcessingFilterCustom(String defaultFilterProcessesUrl,
                                                 AuthenticationManager authenticationManager) {
    super(defaultFilterProcessesUrl, authenticationManager);
  }

  @Override
  public Authentication attemptAuthentication(HttpServletRequest request,
                                              HttpServletResponse response)
          throws AuthenticationException, IOException {
    String method = request.getMethod();
    if (!method.equals("POST")) {
      throw new com.blogitory.blog.security.exception.AuthenticationException(
              "Authentication method not supported: " + method);
    }

    ObjectMapper objectMapper = new ObjectMapper();
    MemberLoginRequestDto loginRequest =
            objectMapper.readValue(request.getInputStream(), MemberLoginRequestDto.class);

    String email = loginRequest.getEmail();
    String password = loginRequest.getPassword();

    UsernamePasswordAuthenticationToken token =
            new UsernamePasswordAuthenticationToken(email, password);

    return getAuthenticationManager().authenticate(token);
  }
}
