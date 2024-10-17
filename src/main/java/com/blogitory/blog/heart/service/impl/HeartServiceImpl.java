package com.blogitory.blog.heart.service.impl;

import com.blogitory.blog.commons.exception.NotFoundException;
import com.blogitory.blog.commons.listener.event.HeartNoticeEvent;
import com.blogitory.blog.heart.entity.Heart;
import com.blogitory.blog.heart.repository.HeartRepository;
import com.blogitory.blog.heart.service.HeartService;
import com.blogitory.blog.member.entity.Member;
import com.blogitory.blog.member.repository.MemberRepository;
import com.blogitory.blog.posts.entity.Posts;
import com.blogitory.blog.posts.repository.PostsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Heart service.
 *
 * @author woonseok
 * @since 1.0
 **/
@Transactional
@Service
@RequiredArgsConstructor
public class HeartServiceImpl implements HeartService {
  private final ApplicationEventPublisher eventPublisher;
  private final HeartRepository heartRepository;
  private final MemberRepository memberRepository;
  private final PostsRepository postsRepository;

  /**
   * {@inheritDoc}
   */
  @Transactional(readOnly = true)
  @Override
  public boolean existHeart(Integer memberNo, String postKey) {
    Heart heart = heartRepository.findByMemberNoAndPostsUrl(memberNo, postKey)
            .orElse(null);

    if (heart == null) {
      return false;
    }

    return !heart.isDeleted();
  }

  /**
   * {@inheritDoc}
   */
  @Transactional(readOnly = true)
  @Override
  public Long countHeart(String postKey) {
    return heartRepository.getHeartCountsByPost(postKey);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void heart(Integer memberNo, String postKey) {
    Member member = memberRepository.findById(memberNo)
            .orElseThrow(() -> new NotFoundException(Member.class));

    Posts posts = postsRepository.findByUrl(postKey)
            .orElseThrow(() -> new NotFoundException(Posts.class));

    Heart heart = heartRepository.findByMemberNoAndPostsUrl(memberNo, postKey)
            .orElse(new Heart(member, posts));

    if (heart.isDeleted()) {
      heart.cancelDelete();
    }

    heart = heartRepository.save(heart);

    Member postsOwner = posts.getCategory().getBlog().getMember();

    if (!member.getMemberNo().equals(postsOwner.getMemberNo())) {
      eventPublisher.publishEvent(new HeartNoticeEvent(heart, postsOwner));
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void deleteHeart(Integer memberNo, String postKey) {
    Heart heart = heartRepository.findByMemberNoAndPostsUrl(memberNo, postKey)
            .orElseThrow(() -> new NotFoundException(Heart.class));

    heart.delete();
  }
}
