package com.blogitory.blog.security.provider;

import com.blogitory.blog.commons.exception.NotFoundException;
import com.blogitory.blog.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Implementation of AuthenticationProvider.
 *
 * @author woonseok
 * @since 1.0
 **/
@RequiredArgsConstructor
public class AuthenticationProviderImpl implements AuthenticationProvider {
  private final UserDetailsService userDetailsService;
  private final MemberService memberService;
  private final PasswordEncoder passwordEncoder;

  @Override
  public Authentication authenticate(Authentication authentication) throws AuthenticationException {

    UsernamePasswordAuthenticationToken authenticationToken =
            (UsernamePasswordAuthenticationToken) authentication;

    String password;

    try {
      password = memberService
              .getPasswordByEmail(authenticationToken.getPrincipal().toString());

    } catch (NotFoundException e) {
      throw new com.blogitory.blog.security.exception.AuthenticationException("Not found User");
    }

    if (!passwordEncoder.matches(authenticationToken.getCredentials().toString(), password)) {
      throw new com.blogitory.blog.security.exception
              .AuthenticationException("Password do not match");
    }

    UserDetails userDetails = userDetailsService.loadUserByUsername(
            (String) authentication.getPrincipal());

    UsernamePasswordAuthenticationToken result =
            new UsernamePasswordAuthenticationToken(
                    userDetails.getUsername(),
                    userDetails.getPassword(),
                    userDetails.getAuthorities());

    result.setDetails(userDetails);

    return result;
  }

  @Override
  public boolean supports(Class<?> authentication) {
    return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
  }
}
