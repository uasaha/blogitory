package com.blogitory.blog.commons.aspect;

import com.blogitory.blog.security.exception.AuthorizationException;
import com.blogitory.blog.security.util.SecurityUtils;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

/**
 * Restrict access to content to need specific role.
 *
 * @author woonseok
 * @since 1.0
 **/
@Slf4j
@Aspect
@Component
public class AuthorizationAspect {
  private static final String ROLE_ADMIN = "ROLE_ADMIN";
  private static final String ROLE_CONTRIBUTOR = "ROLE_CONTRIBUTOR";
  private static final String ROLE_WRITER = "ROLE_WRITER";
  private static final String ROLE_USER = "ROLE_USER";

  /**
   * Restrict access to admin-only content.
   *
   * @param pjp admin-only controller
   * @return proceed
   * @throws Throwable ?
   */
  @Around(value = "@annotation(com.blogitory.blog.commons.annotaion.RoleAdmin)")
  public Object checkRoleAdmin(ProceedingJoinPoint pjp) throws Throwable {
    List<? extends GrantedAuthority> authorities = SecurityUtils.getCurrentAuthorities();

    if (isContains(authorities, ROLE_ADMIN)) {
      return pjp.proceed();
    }

    throw new AuthorizationException();
  }

  /**
   * Restrict access to contributor-only content.
   *
   * @param pjp contributor-only controller
   * @return proceed
   * @throws Throwable ?
   */
  @Around(value = "@annotation(com.blogitory.blog.commons.annotaion.RoleContributor)")
  public Object checkRoleContributor(ProceedingJoinPoint pjp) throws Throwable {
    List<? extends GrantedAuthority> authorities = SecurityUtils.getCurrentAuthorities();

    if (isContains(authorities, ROLE_CONTRIBUTOR)) {
      return pjp.proceed();
    }

    throw new AuthorizationException();
  }

  /**
   * Restrict access to writer-only content.
   *
   * @param pjp writer-only controller
   * @return proceed
   * @throws Throwable ?
   */
  @Around(value = "@annotation(com.blogitory.blog.commons.annotaion.RoleWriter)")
  public Object checkRoleWriter(ProceedingJoinPoint pjp) throws Throwable {
    List<? extends GrantedAuthority> authorities = SecurityUtils.getCurrentAuthorities();

    if (isContains(authorities, ROLE_WRITER)) {
      return pjp.proceed();
    }

    throw new AuthorizationException();
  }

  /**
   * Restrict access to user-only content.
   *
   * @param pjp user-only controller
   * @return proceed
   * @throws Throwable ?
   */
  @Around(value = "@annotation(com.blogitory.blog.commons.annotaion.RoleUser)")
  public Object checkRoleUser(ProceedingJoinPoint pjp) throws Throwable {
    List<? extends GrantedAuthority> authorities = SecurityUtils.getCurrentAuthorities();

    if (isContains(authorities, ROLE_USER)) {
      return pjp.proceed();
    }

    throw new AuthorizationException();
  }

  /**
   * checking for authorities to access content.
   *
   * @param authorities   user's role
   * @param needAuthority needed
   * @return is accessible
   */
  private boolean isContains(List<? extends GrantedAuthority> authorities, String needAuthority) {
    return authorities.stream()
            .anyMatch(role -> needAuthority.equals(role.getAuthority()));
  }
}
