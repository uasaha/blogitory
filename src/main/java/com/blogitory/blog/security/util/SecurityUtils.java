package com.blogitory.blog.security.util;

import java.util.List;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * Util class for Security.
 *
 * @author woonseok
 * @since 1.0
 **/
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SecurityUtils {

  private static final String ANONYMOUS = "anonymousUser";

  /**
   * Get user no from security context.
   *
   * @return user no
   */
  public static Integer getCurrentUserNo() {
    return Integer.parseInt(SecurityContextHolder.getContext().getAuthentication().getName());
  }

  /**
   * Get roles from security context.
   *
   * @return roles
   */
  public static List<? extends GrantedAuthority> getCurrentAuthorities() {
    return SecurityContextHolder.getContext().getAuthentication().getAuthorities()
            .stream().toList();
  }

  /**
   * Check is authenticated.
   *
   * @return boolean
   */
  public static boolean isAuthenticated() {
    String name = SecurityContextHolder.getContext().getAuthentication().getName();

    if (name == null) {
      return false;
    }

    return !name.equals(ANONYMOUS);
  }
}
