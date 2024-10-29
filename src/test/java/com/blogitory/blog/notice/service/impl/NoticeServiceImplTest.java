package com.blogitory.blog.notice.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.blogitory.blog.blog.entity.Blog;
import com.blogitory.blog.blog.entity.BlogDummy;
import com.blogitory.blog.category.entity.Category;
import com.blogitory.blog.category.entity.CategoryDummy;
import com.blogitory.blog.comment.entity.Comment;
import com.blogitory.blog.comment.entity.CommentDummy;
import com.blogitory.blog.commons.dto.Pages;
import com.blogitory.blog.commons.publisher.RedisPublisher;
import com.blogitory.blog.follow.repository.FollowRepository;
import com.blogitory.blog.heart.entity.Heart;
import com.blogitory.blog.heart.entity.HeartDummy;
import com.blogitory.blog.member.entity.Member;
import com.blogitory.blog.member.entity.MemberDummy;
import com.blogitory.blog.notice.dto.GetNoticeResponseDto;
import com.blogitory.blog.notice.entity.Notice;
import com.blogitory.blog.notice.entity.NoticeDummy;
import com.blogitory.blog.notice.enums.NoticeType;
import com.blogitory.blog.notice.repository.NoticeRepository;
import com.blogitory.blog.notice.service.NoticeService;
import com.blogitory.blog.posts.entity.Posts;
import com.blogitory.blog.posts.entity.PostsDummy;
import com.blogitory.blog.security.exception.AuthorizationException;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.test.util.ReflectionTestUtils;

/**
 * NoticeServiceTest.
 *
 * @author woonseok
 * @since 1.0
 **/
class NoticeServiceImplTest {
  NoticeService noticeService;
  NoticeRepository noticeRepository;
  FollowRepository followRepository;
  RedisPublisher redisPublisher;

  @BeforeEach
  void setUp() {
    noticeRepository = mock(NoticeRepository.class);
    followRepository = mock(FollowRepository.class);
    redisPublisher = mock(RedisPublisher.class);
    noticeService = new NoticeServiceImpl(noticeRepository, followRepository, redisPublisher);
  }

  @Test
  void getAllNotice() {
    Member member = MemberDummy.dummy();
    Notice notice = NoticeDummy.dummy(member);

    when(noticeRepository.findAllNoticeByMemberNo(any(), anyInt())).thenReturn(new PageImpl<>(List.of(notice)));

    Pages<GetNoticeResponseDto> result = noticeService.getAllNotice(Pageable.ofSize(10), member.getMemberNo());

    assertEquals(notice.isRead(), result.body().getFirst().isRead());
  }

  @Test
  void hasNonReadNotice() {
    when(noticeRepository.hasNonReadNotice(anyInt())).thenReturn(true);

    boolean result = noticeService.hasNonReadNotice(1);
    assertTrue(result);
  }

  @Test
  void read() {
    Member member = MemberDummy.dummy();
    Notice notice = NoticeDummy.dummy(member);

    when(noticeRepository.findById(any()))
            .thenReturn(Optional.of(notice));

    noticeService.read(member.getMemberNo(), notice.getNoticeNo());

    assertTrue(notice.isRead());
  }

  @Test
  void readFailed() {
    Member member = MemberDummy.dummy();
    Notice notice = NoticeDummy.dummy(member);

    when(noticeRepository.findById(any()))
            .thenReturn(Optional.of(notice));

    assertThrows(AuthorizationException.class,
            () -> noticeService.read(2, 1L));
  }

  @Test
  void delete() {
    Member member = MemberDummy.dummy();
    Notice notice = NoticeDummy.dummy(member);

    when(noticeRepository.findById(any()))
            .thenReturn(Optional.of(notice));

    noticeService.delete(member.getMemberNo(), notice.getNoticeNo());

    verify(noticeRepository, times(1)).delete(any(Notice.class));
  }

  @Test
  void followNotice() {
    Member member = MemberDummy.dummy();
    Member followFrom = MemberDummy.dummy();

    Notice notice = NoticeDummy.dummy(member);

    when(noticeRepository.save(any())).thenReturn(notice);

    noticeService.followNotice(followFrom, member);

    verify(noticeRepository, times(1)).save(any(Notice.class));
    verify(redisPublisher, times(1)).noticePublish(any());
  }

  @Test
  void commentNotice() {
    Member member = MemberDummy.dummy();
    Blog blog = BlogDummy.dummy(member);
    Category category = CategoryDummy.dummy(blog);
    Posts posts = PostsDummy.dummy(category);

    Member commentFrom = MemberDummy.dummy();

    Comment comment = CommentDummy.dummy(commentFrom, posts);

    Notice notice = NoticeDummy.dummy(member);
    ReflectionTestUtils.setField(notice, "type", NoticeType.COMMENT);

    when(noticeRepository.save(any())).thenReturn(notice);

    noticeService.commentNotice("sender", comment, member);

    verify(noticeRepository, times(1)).save(any(Notice.class));
    verify(redisPublisher, times(1)).noticePublish(any());
  }

  @Test
  void heartNotice() {
    Member member = MemberDummy.dummy();
    Blog blog = BlogDummy.dummy(member);
    Category category = CategoryDummy.dummy(blog);
    Posts posts = PostsDummy.dummy(category);

    Member heartFrom = MemberDummy.dummy();
    Heart heart = HeartDummy.dummy(heartFrom, posts);

    Notice notice = NoticeDummy.dummy(member);
    ReflectionTestUtils.setField(notice, "type", NoticeType.COMMENT);

    when(noticeRepository.save(any())).thenReturn(notice);

    noticeService.heartNotice(heart, member);

    verify(noticeRepository, times(1)).save(any(Notice.class));
    verify(redisPublisher, times(1)).noticePublish(any());
  }

  @Test
  void newPostsNotice() {
    Member member = MemberDummy.dummy();
    Blog blog = BlogDummy.dummy(member);
    Category category = CategoryDummy.dummy(blog);
    Posts posts = PostsDummy.dummy(category);
    Notice notice = NoticeDummy.dummy(member);

    Member follower = MemberDummy.dummy();

    when(followRepository.findFollowersByMemberNo(anyInt()))
            .thenReturn(List.of(follower));
    when(noticeRepository.save(any())).thenReturn(notice);

    noticeService.newPostsNotice(posts);

    verify(noticeRepository, times(1)).save(any(Notice.class));
    verify(redisPublisher, times(1)).noticePublish(any());
  }
}