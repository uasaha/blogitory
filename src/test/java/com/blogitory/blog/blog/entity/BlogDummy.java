package com.blogitory.blog.blog.entity;

import com.blogitory.blog.member.entity.Member;

/**
 * Blog dummy.
 *
 * @author woonseok
 * @since 1.0
 **/
public class BlogDummy {
  public static Blog dummy(Member member) {
    return new Blog(
            1L,
            member,
            "blogName",
            "urlName",
            "background",
            "intro",
            "light");
  }

}