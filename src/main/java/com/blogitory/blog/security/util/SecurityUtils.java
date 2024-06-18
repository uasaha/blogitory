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
   * Get username from security context.
   *
   * @return username
   */
  public static String getCurrentUsername() {
    UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext()
            .getAuthentication().getDetails();

    return userDetails.getUsername();
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
   * Get email from security context.
   *
   * @return email
   */
  public static String getCurrentUserEmail() {
    return SecurityContextHolder.getContext().getAuthentication().getCredentials().toString();
  }
}
