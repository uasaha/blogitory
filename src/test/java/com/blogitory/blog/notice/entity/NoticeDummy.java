package com.blogitory.blog.notice.entity;

import com.blogitory.blog.member.entity.Member;

/**
 * Notice dummy.
 *
 * @author woonseok
 * @since 1.0
 **/
public class NoticeDummy {
  public static Notice dummy(Member member) {
    return new Notice(1L,
            member,
            1L,
            "type",
            "msg",
            false);
  }
}