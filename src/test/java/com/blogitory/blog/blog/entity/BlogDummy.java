package com.blogitory.blog.blog.entity;

import com.blogitory.blog.member.entity.Member;
import java.time.LocalDateTime;

/**
 * Blog dummy.
 *
 * @author woonseok
 * @since 1.0
 */
public class BlogDummy {
  /**
   * Dummy blog.
   *
   * @param member the member
   * @return the blog
   */
  public static Blog dummy(Member member) {
    return new Blog(
            1L,
            member,
            "blog_name",
            "blog bio",
            "urlName",
            "background",
            "intro",
            "light",
            false,
            LocalDateTime.of(2000, 12, 10, 10, 30));
  }

}