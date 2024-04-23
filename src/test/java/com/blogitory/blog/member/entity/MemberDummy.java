package com.blogitory.blog.member.entity;

/**
 * Test data of Member.
 *
 * @author woonseok
 * @since 1.0
 **/
public class MemberDummy {
  public static Member dummy() {
    return new Member(
            1,
            "test@test.com",
            "password!@#",
            "dummy",
            "profileThumb",
            "test@test.com",
            "github",
            "twitter",
            "facebook",
            "homepage",
            false,
            false);
  }
}
