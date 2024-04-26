package com.blogitory.blog.follow.entity;

import com.blogitory.blog.member.entity.Member;

/**
 * Follow Dummy.
 *
 * @author woonseok
 * @since 1.0
 **/
public class FollowDummy {
  public static Follow dummy(Member follower, Member leader) {
    return new Follow(1L, follower, leader);
  }
}