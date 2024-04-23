package com.blogitory.blog.security.users;

import java.util.Collection;
import java.util.List;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * Implementation of UserDetails.
 *
 * @author woonseok
 * @since 1.0
 **/
@Getter
public class UserDetailsImpl implements UserDetails {
  private final String username; //email
  private final String nickname;
  private final Integer userNo;
  private final String password;
  private final boolean accountNonLocked = true;
  private final boolean accountNonExpired = true;
  private final boolean credentialsNonExpired = true;
  private final boolean enabled = true;
  private Collection<? extends GrantedAuthority> authorities;

  /**
   * Constructor of UserDetailsImpl.
   *
   * @param email       email
   * @param password    password
   * @param userNo      user no
   * @param nickname    username
   * @param authorities authorities
   */
  public UserDetailsImpl(String email,
                         String password,
                         Integer userNo,
                         String nickname,
                         List<? extends GrantedAuthority> authorities) {
    this.username = email;
    this.password = password;
    this.userNo = userNo;
    this.nickname = nickname;
    this.authorities = authorities;
  }
}
