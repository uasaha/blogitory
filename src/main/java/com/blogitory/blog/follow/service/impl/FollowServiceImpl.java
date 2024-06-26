package com.blogitory.blog.follow.service.impl;

import com.blogitory.blog.follow.repository.FollowRepository;
import com.blogitory.blog.follow.service.FollowService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * 설명 작성 필!
 *
 * @author woonseok
 * @since 1.0
 **/
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FollowServiceImpl implements FollowService {
  private final FollowRepository followRepository;

  @Override
  @Transactional(readOnly = true, propagation = Propagation.MANDATORY)
  public Long getFollowerCnt(Integer followToNo) {
    return followRepository.countFollower(followToNo);
  }

  @Override
  @Transactional(readOnly = true, propagation = Propagation.MANDATORY)
  public Long getFollowingCnt(Integer followFromNo) {
    return followRepository.countFollowee(followFromNo);
  }
}
