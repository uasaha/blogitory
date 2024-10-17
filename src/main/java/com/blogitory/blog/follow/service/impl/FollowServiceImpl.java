package com.blogitory.blog.follow.service.impl;

import com.blogitory.blog.commons.exception.NotFoundException;
import com.blogitory.blog.commons.listener.event.FollowNoticeEvent;
import com.blogitory.blog.follow.dto.response.GetAllFollowResponseDto;
import com.blogitory.blog.follow.dto.response.GetFollowResponseDto;
import com.blogitory.blog.follow.entity.Follow;
import com.blogitory.blog.follow.repository.FollowRepository;
import com.blogitory.blog.follow.service.FollowService;
import com.blogitory.blog.member.entity.Member;
import com.blogitory.blog.member.repository.MemberRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementation of Follow service.
 *
 * @author woonseok
 * @since 1.0
 **/
@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class FollowServiceImpl implements FollowService {

  private final ApplicationEventPublisher eventPublisher;
  private final MemberRepository memberRepository;
  private final FollowRepository followRepository;

  /**
   * {@inheritDoc}
   */
  @Override
  public void follow(Integer followFrom, String followToUsername) {
    if (followRepository.findByFromNoAndToUsername(followFrom, followToUsername).isPresent()) {
      return;
    }

    Member followFromMember = memberRepository.findById(followFrom)
            .orElseThrow(() -> new NotFoundException(Member.class));

    Member followToMember = memberRepository.findByUsername(followToUsername)
            .orElseThrow(() -> new NotFoundException(Member.class));

    Follow follow = new Follow(null, followToMember, followFromMember);

    followRepository.save(follow);

    eventPublisher.publishEvent(new FollowNoticeEvent(followFromMember, followToMember));
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

  /**
   * {@inheritDoc}
   */
  @Override
  @Transactional(readOnly = true)
  public boolean isFollowed(Integer followFrom, String followToUsername) {
    return followRepository.findByFromNoAndToUsername(followFrom, followToUsername)
            .isPresent();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  @Transactional(readOnly = true)
  public GetAllFollowResponseDto getAllFollowInfo(Integer memberNo, String username) {
    Member member = null;
    boolean isMine = false;

    if (memberNo != null) {
      member = memberRepository.findById(memberNo)
              .orElse(null);
    }

    if (member != null) {
      isMine = member.getUsername().equals(username);
    }

    List<GetFollowResponseDto> followers = followRepository
            .getAllFollowerByToUsername(username);
    List<GetFollowResponseDto> followings = followRepository
            .getAllFollowingByFromUsername(username);

    return new GetAllFollowResponseDto(isMine, followers, followings);
  }
}
