package com.blogitory.blog.follow.service;

/**
 * Follow service.
 *
 * @author woonseok
 * @Date 2024-08-26
 * @since 1.0
 **/
public interface FollowService {

  /**
   * Do follow.
   *
   * @param followFrom       follower
   * @param followToUsername followee
   */
  void follow(Integer followFrom, String followToUsername);

  /**
   * Do unfollow.
   *
   * @param memberNo follower
   * @param username followee
   */
  void unFollow(Integer memberNo, String username);
}
