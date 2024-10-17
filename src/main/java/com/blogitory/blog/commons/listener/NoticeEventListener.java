package com.blogitory.blog.commons.listener;

import com.blogitory.blog.commons.listener.event.CommentNoticeEvent;
import com.blogitory.blog.commons.listener.event.FollowNoticeEvent;
import com.blogitory.blog.commons.listener.event.HeartNoticeEvent;
import com.blogitory.blog.commons.listener.event.NewPostsNoticeEvent;
import com.blogitory.blog.commons.listener.event.SseSendEvent;
import com.blogitory.blog.notice.service.NoticeService;
import com.blogitory.blog.sse.service.SseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

/**
 * Notice event listener.
 *
 * @author woonseok
 * @since 1.0
 **/
@Slf4j
@RequiredArgsConstructor
@Component
public class NoticeEventListener {
  private final NoticeService noticeService;
  private final SseService sseService;

  /**
   * Event when create follow.
   *
   * @param noticeEvent notice event
   */
  @Async
  @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
  public void followNoticeEvent(FollowNoticeEvent noticeEvent) {
    noticeService.followNotice(
            noticeEvent.followFrom(),
            noticeEvent.followTo());
  }

  /**
   * Event when create comment.
   *
   * @param noticeEvent notice event
   */
  @Async
  @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
  public void commentNoticeEvent(CommentNoticeEvent noticeEvent) {
    noticeService.commentNotice(
            noticeEvent.name(),
            noticeEvent.comment(),
            noticeEvent.commentTo());
  }

  /**
   * Event when create heart.
   *
   * @param noticeEvent notice event
   */
  @Async
  @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
  public void heartNoticeEvent(HeartNoticeEvent noticeEvent) {
    noticeService.heartNotice(noticeEvent.heart(), noticeEvent.heartTo());
  }

  /**
   * Event when create new posts to followers.
   *
   * @param noticeEvent notice event
   */
  @Async
  @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
  public void newPostsNoticeEvent(NewPostsNoticeEvent noticeEvent) {
    noticeService.newPostsNotice(noticeEvent.posts(), noticeEvent.targets());
  }

  /**
   * Event when send notification through SSE.
   *
   * @param sendEvent sse event
   */
  @Async
  @EventListener
  public void sseSendEvent(SseSendEvent sendEvent) {
    sseService.send(sendEvent.noticeNo(), sendEvent.target(), sendEvent.body());
  }
}
