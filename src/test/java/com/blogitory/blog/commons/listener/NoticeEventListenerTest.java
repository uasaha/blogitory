package com.blogitory.blog.commons.listener;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.blogitory.blog.blog.entity.Blog;
import com.blogitory.blog.blog.entity.BlogDummy;
import com.blogitory.blog.category.entity.Category;
import com.blogitory.blog.category.entity.CategoryDummy;
import com.blogitory.blog.comment.entity.Comment;
import com.blogitory.blog.comment.entity.CommentDummy;
import com.blogitory.blog.commons.listener.event.CommentNoticeEvent;
import com.blogitory.blog.commons.listener.event.FollowNoticeEvent;
import com.blogitory.blog.commons.listener.event.HeartNoticeEvent;
import com.blogitory.blog.commons.listener.event.NewPostsNoticeEvent;
import com.blogitory.blog.commons.listener.event.SseSendEvent;
import com.blogitory.blog.heart.entity.Heart;
import com.blogitory.blog.heart.entity.HeartDummy;
import com.blogitory.blog.member.entity.Member;
import com.blogitory.blog.member.entity.MemberDummy;
import com.blogitory.blog.notice.service.NoticeService;
import com.blogitory.blog.posts.entity.Posts;
import com.blogitory.blog.posts.entity.PostsDummy;
import com.blogitory.blog.sse.service.SseService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * NoticeEventListenerTest.
 *
 * @author woonseok
 * @since 1.0
 **/
class NoticeEventListenerTest {
  NoticeEventListener noticeEventListener;
  NoticeService noticeService;
  SseService sseService;

  @BeforeEach
  void setUp() {
    noticeService = mock(NoticeService.class);
    sseService = mock(SseService.class);
    noticeEventListener = new NoticeEventListener(noticeService, sseService);
  }

  @Test
  void followNoticeEvent() {
    doNothing().when(noticeService).followNotice(any(), any());

    Member member = MemberDummy.dummy();
    Member follower = MemberDummy.dummy();

    FollowNoticeEvent followNoticeEvent = new FollowNoticeEvent(member, follower);

    noticeEventListener.followNoticeEvent(followNoticeEvent);

    verify(noticeService, times(1)).followNotice(any(), any());
  }

  @Test
  void commentNoticeEvent() {
    doNothing().when(noticeService).commentNotice(anyString(), any(), any());

    Member member = MemberDummy.dummy();
    Blog blog = BlogDummy.dummy(member);
    Category category = CategoryDummy.dummy(blog);
    Posts posts = PostsDummy.dummy(category);
    Comment comment = CommentDummy.dummy(member, posts);

    CommentNoticeEvent commentNoticeEvent = new CommentNoticeEvent("name", comment, member);

    noticeEventListener.commentNoticeEvent(commentNoticeEvent);

    verify(noticeService, times(1)).commentNotice(anyString(), any(), any());
  }

  @Test
  void heartNoticeEvent() {
    doNothing().when(noticeService).heartNotice(any(), any());

    Member member = MemberDummy.dummy();
    Blog blog = BlogDummy.dummy(member);
    Category category = CategoryDummy.dummy(blog);
    Posts posts = PostsDummy.dummy(category);
    Heart heart = HeartDummy.dummy(member, posts);

    HeartNoticeEvent heartNoticeEvent = new HeartNoticeEvent(heart, member);

    noticeEventListener.heartNoticeEvent(heartNoticeEvent);

    verify(noticeService, times(1)).heartNotice(any(), any());
  }

  @Test
  void newPostsNoticeEvent() {
    doNothing().when(noticeService).newPostsNotice(any());
    Member member = MemberDummy.dummy();
    Blog blog = BlogDummy.dummy(member);
    Category category = CategoryDummy.dummy(blog);
    Posts posts = PostsDummy.dummy(category);

    NewPostsNoticeEvent noticeEvent = new NewPostsNoticeEvent(posts);

    noticeEventListener.newPostsNoticeEvent(noticeEvent);

    verify(noticeService, times(1)).newPostsNotice(any());
  }

  @Test
  void sseSendEvent() {
    doNothing().when(sseService).send(anyLong(), any(), any());

    SseSendEvent sendEvent = new SseSendEvent(1L, 1, "body");

    noticeEventListener.sseSendEvent(sendEvent);

    verify(sseService, times(1)).send(1L, 1, "body");
  }
}