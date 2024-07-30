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
  private final String idName; //username
  private final String name;
  private final Integer userNo;
  private final String pfp;
  private final String password;
  private final boolean accountNonLocked;
  private final boolean accountNonExpired;
  private final boolean credentialsNonExpired;
  private final boolean enabled;
  private final Collection<? extends GrantedAuthority> authorities;

  /**
   * Constructor of UserDetailsImpl.
   *
   * @param email       email
   * @param password    password
   * @param userNo      user no
   * @param username    username
   * @param name        name
   * @param authorities authorities
   */
  public UserDetailsImpl(String email,
                         String password,
                         Integer userNo,
                         String username,
                         String name,
                         String pfp,
                         List<? extends GrantedAuthority> authorities) {
    this.username = email;
    this.password = password;
    this.userNo = userNo;
    this.idName = username;
    this.name = name;
    this.pfp = pfp;
    this.authorities = authorities;
    this.accountNonLocked = true;
    this.accountNonExpired = true;
    this.credentialsNonExpired = true;
    this.enabled = true;
  }
}
