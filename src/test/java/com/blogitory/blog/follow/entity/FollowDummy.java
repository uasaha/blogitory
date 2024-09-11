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
   * @param followToNo the follower
   * @param followFromNo   the leader
   * @return the follow
   */
  public static Follow dummy(Member followToNo, Member followFromNo) {
    return new Follow(1L, followToNo, followFromNo);
  }
}