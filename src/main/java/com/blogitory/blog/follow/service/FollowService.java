package com.blogitory.blog.follow.service;

import com.blogitory.blog.follow.dto.response.GetAllFollowResponseDto;

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

  /**
   * Get is followed.
   *
   * @param followFrom       from member no
   * @param followToUsername to member username
   * @return is followed
   */
  boolean isFollowed(Integer followFrom, String followToUsername);

  /**
   * Get all follow infos.
   *
   * @param memberNo current member no
   * @param username find username
   * @return follow infos
   */
  GetAllFollowResponseDto getAllFollowInfo(Integer memberNo, String username);
}
