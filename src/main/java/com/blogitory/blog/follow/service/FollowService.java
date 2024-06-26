package com.blogitory.blog.follow.service;

/**
 * 설명 작성 필!
 *
 * @author woonseok
 * @since 1.0
 **/
public interface FollowService {
  Long getFollowerCnt(Integer followToNo);

  Long getFollowingCnt(Integer followFromNo);
}
