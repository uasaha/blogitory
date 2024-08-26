package com.blogitory.blog.security.util;

import com.blogitory.blog.security.users.UserDetailsImpl;
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

  public static boolean isAuthenticated() {
    return SecurityContextHolder.getContext().getAuthentication()
            .getDetails() instanceof UserDetailsImpl;
  }
}
