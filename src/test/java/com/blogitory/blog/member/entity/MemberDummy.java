package com.blogitory.blog.member.entity;

/**
 * Test data of Member.
 *
 * @author woonseok
 * @since 1.0
 */
public class MemberDummy {
  /**
   * Dummy member.
   *
   * @return the member
   */
  public static Member dummy() {
    return new Member(
            1,
            "test@test.com",
            "password!@#",
            "dummy",
            "dummy",
            "dummy bio",
            "profileThumb",
            "test@test.com",
            false,
            false,
            null,
            true,
            true,
            true,
            true);
  }
}
