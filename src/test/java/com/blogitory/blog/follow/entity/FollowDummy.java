package com.blogitory.blog.follow.entity;

import com.blogitory.blog.member.entity.Member;

/**
 * Follow Dummy.
 *
 * @author woonseok
 * @since 1.0
 */
public class FollowDummy {
  /**
   * Dummy follow.
   *
   * @param follower the follower
   * @param leader   the leader
   * @return the follow
   */
  public static Follow dummy(Member follower, Member leader) {
    return new Follow(1L, follower, leader);
  }
}