package com.blogitory.blog.role.entity;

/**
 * Test data of Role.
 *
 * @author woonseok
 * @since 1.0
 */
public class RoleDummy {
  /**
   * Dummy role.
   *
   * @return the role
   */
  public static Role dummy() {
    return new Role(1, "ROLE_DUMMY");
  }
}
