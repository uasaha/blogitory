package com.blogitory.blog.notice.service.impl;

import static com.blogitory.blog.commons.config.RedisConfig.NOTIFICATION_CHANNEL;

import com.blogitory.blog.comment.entity.Comment;
import com.blogitory.blog.commons.dto.Pages;
import com.blogitory.blog.commons.exception.NotFoundException;
import com.blogitory.blog.commons.listener.event.SseSendEvent;
import com.blogitory.blog.commons.publisher.RedisPublisher;
import com.blogitory.blog.follow.repository.FollowRepository;
import com.blogitory.blog.heart.entity.Heart;
import com.blogitory.blog.member.entity.Member;
import com.blogitory.blog.notice.dto.DefaultNoticeResponseDto;
import com.blogitory.blog.notice.dto.GetNoticeResponseDto;
import com.blogitory.blog.notice.entity.Notice;
import com.blogitory.blog.notice.enums.NoticeType;
import com.blogitory.blog.notice.repository.NoticeRepository;
import com.blogitory.blog.notice.service.NoticeService;
import com.blogitory.blog.posts.entity.Posts;
import com.blogitory.blog.security.exception.AuthorizationException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementation of notice service.
 *
 * @author woonseok
 * @since 1.0
 **/
@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class NoticeServiceImpl implements NoticeService {

  private final NoticeRepository noticeRepository;
  private final FollowRepository followRepository;
  private final RedisPublisher redisPublisher;

  /**
   * {@inheritDoc}
   */
  @Transactional(readOnly = true)
  @Override
  public Pages<GetNoticeResponseDto> getAllNotice(Pageable pageable, Integer memberNo) {
    Page<Notice> notices = noticeRepository.findAllNoticeByMemberNo(pageable, memberNo);

    List<GetNoticeResponseDto> result = notices.getContent().stream()
            .map(n -> new GetNoticeResponseDto(n.getNoticeNo(),
                    DefaultNoticeResponseDto.of(n), n.isRead(), n.getCreatedAt()))
            .toList();

    return new Pages<>(result,
            pageable.getPageNumber(),
            notices.hasPrevious(),
            notices.hasNext(),
            notices.getTotalElements());
  }

  /**
   * {@inheritDoc}
   */
  @Transactional(readOnly = true)
  @Override
  public boolean hasNonReadNotice(Integer memberNo) {
    return noticeRepository.hasNonReadNotice(memberNo);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void read(Integer memberNo, Long noticeNo) {
    Notice notice = noticeRepository.findById(noticeNo)
            .orElseThrow(() -> new NotFoundException(Notice.class));

    if (!notice.getMember().getMemberNo().equals(memberNo)) {
      throw new AuthorizationException();
    }

    notice.read();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void delete(Integer memberNo, Long noticeNo) {
    Notice notice = noticeRepository.findById(noticeNo)
            .orElseThrow(() -> new NotFoundException(Notice.class));

    noticeRepository.delete(notice);
  }

  /**
   * {@inheritDoc}
   */
  @Transactional(propagation = Propagation.REQUIRES_NEW)
  @Override
  public void followNotice(Member followFrom, Member followTo) {
    String url = "/@" + followFrom.getUsername();
    Notice notice = Notice.builder()
            .member(followTo)
            .sender(followFrom.getUsername())
            .type(NoticeType.FOLLOW)
            .url(url)
            .build();

    notice = noticeRepository.save(notice);

    if (followTo.isFollowAlert()) {
      publishEvent(notice.getNoticeNo(), followTo.getMemberNo(),
              DefaultNoticeResponseDto.of(notice));
    }
  }

  /**
   * {@inheritDoc}
   */
  @Transactional(propagation = Propagation.REQUIRES_NEW)
  @Override
  public void commentNotice(String name, Comment comment, Member commentTo) {
    String url = "/" + comment.getPosts().getUrl();
    Notice notice = Notice.builder()
            .member(commentTo)
            .sender(comment.getMember().getUsername())
            .type(NoticeType.COMMENT)
            .url(url)
            .content(comment.getContents())
            .build();

    notice = noticeRepository.save(notice);

    if (commentTo.isCommentAlert()) {
      publishEvent(notice.getNoticeNo(), commentTo.getMemberNo(),
              DefaultNoticeResponseDto.of(notice));
    }
  }

  /**
   * {@inheritDoc}
   */
  @Transactional(propagation = Propagation.REQUIRES_NEW)
  @Override
  public void heartNotice(Heart heart, Member heartTo) {
    Member heartFrom = heart.getMember();
    String url = "/" + heart.getPosts().getUrl();
    Notice notice = Notice.builder()
            .member(heartTo)
            .sender(heartFrom.getUsername())
            .type(NoticeType.HEART)
            .url(url)
            .build();

    notice = noticeRepository.save(notice);

    if (heartTo.isHeartAlert()) {
      publishEvent(notice.getNoticeNo(), heartTo.getMemberNo(),
              DefaultNoticeResponseDto.of(notice));
    }
  }

  /**
   * {@inheritDoc}
   */
  @Transactional(propagation = Propagation.REQUIRES_NEW)
  @Override
  public void newPostsNotice(Posts posts, List<Member> targets) {
    String url = "/" + posts.getUrl();
    Member member = posts.getCategory().getBlog().getMember();

    List<Member> target = followRepository.findFollowersByMemberNo(member.getMemberNo());

    target.forEach(t -> {
      Notice notice = Notice.builder()
              .member(t)
              .sender(member.getUsername())
              .type(NoticeType.NEW_POSTS)
              .url(url)
              .content(posts.getSubject())
              .build();

      notice = noticeRepository.save(notice);

      if (t.isNewAlert()) {
        publishEvent(notice.getNoticeNo(), t.getMemberNo(),
                DefaultNoticeResponseDto.of(notice));
      }
    });
  }

  /**
   * Publish event for send to sse.
   *
   * @param noticeNo notice no
   * @param target   target member no
   * @param body     notice response
   */
  private void publishEvent(Long noticeNo, Integer target, Object body) {
    SseSendEvent sendEvent = new SseSendEvent(noticeNo, target, body);
    redisPublisher.publish(NOTIFICATION_CHANNEL, sendEvent);
  }
}