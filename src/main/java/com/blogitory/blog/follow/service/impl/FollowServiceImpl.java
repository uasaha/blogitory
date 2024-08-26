package com.blogitory.blog.follow.service.impl;

import com.blogitory.blog.commons.exception.NotFoundException;
import com.blogitory.blog.follow.entity.Follow;
import com.blogitory.blog.follow.repository.FollowRepository;
import com.blogitory.blog.follow.service.FollowService;
import com.blogitory.blog.member.entity.Member;
import com.blogitory.blog.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementation of Follow service.
 *
 * @author woonseok
 * @Date 2024-08-26
 * @since 1.0
 **/
@Service
@Transactional
@RequiredArgsConstructor
public class FollowServiceImpl implements FollowService {
  private final MemberRepository memberRepository;
  private final FollowRepository followRepository;

  /**
   * {@inheritDoc}
   */
  @Override
  public void follow(Integer followFrom, String followToUsername) {
    Member followFromMember = memberRepository.findById(followFrom)
            .orElseThrow(() -> new NotFoundException(Member.class));

    Member followToMember = memberRepository.findByUsername(followToUsername)
            .orElseThrow(() -> new NotFoundException(Member.class));

    Follow follow = new Follow(null, followToMember, followFromMember);

    followRepository.save(follow);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void unFollow(Integer followFrom, String followToUsername) {
    Follow follow = followRepository
            .findByFromNoAndToUsername(followFrom, followToUsername)
            .orElseThrow(() -> new NotFoundException(Follow.class));

    followRepository.delete(follow);
  }
}
