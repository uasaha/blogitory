package com.blogitory.blog.security.provider;

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
  private final PasswordEncoder passwordEncoder;

  @Override
  public Authentication authenticate(Authentication authentication) throws AuthenticationException {

    UserDetails userDetails = userDetailsService.loadUserByUsername(
            (String) authentication.getPrincipal());

    if (!passwordEncoder.matches(authentication.getCredentials().toString(),
            userDetails.getPassword())) {
      throw new com.blogitory.blog.security.exception
              .AuthenticationException("Password not matched");
    }

    UsernamePasswordAuthenticationToken token =
            new UsernamePasswordAuthenticationToken(
                    userDetails.getUsername(),
                    userDetails.getPassword(),
                    userDetails.getAuthorities());

    token.setDetails(userDetails);

    return token;
  }

  @Override
  public boolean supports(Class<?> authentication) {
    return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
  }
}
