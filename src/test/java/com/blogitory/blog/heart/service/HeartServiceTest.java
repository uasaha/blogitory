package com.blogitory.blog.heart.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.blogitory.blog.blog.entity.Blog;
import com.blogitory.blog.blog.entity.BlogDummy;
import com.blogitory.blog.category.entity.Category;
import com.blogitory.blog.category.entity.CategoryDummy;
import com.blogitory.blog.heart.entity.Heart;
import com.blogitory.blog.heart.entity.HeartDummy;
import com.blogitory.blog.heart.repository.HeartRepository;
import com.blogitory.blog.heart.service.impl.HeartServiceImpl;
import com.blogitory.blog.member.entity.Member;
import com.blogitory.blog.member.entity.MemberDummy;
import com.blogitory.blog.member.repository.MemberRepository;
import com.blogitory.blog.posts.entity.Posts;
import com.blogitory.blog.posts.entity.PostsDummy;
import com.blogitory.blog.posts.repository.PostsRepository;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

/**
 * Heart service test.
 *
 * @author woonseok
 * @Date 2024-08-29
 * @since 1.0
 **/
class HeartServiceTest {
  HeartRepository heartRepository;
  MemberRepository memberRepository;
  PostsRepository postsRepository;
  HeartService heartService;

  @BeforeEach
  void setUp() {
    heartRepository = mock(HeartRepository.class);
    memberRepository = mock(MemberRepository.class);
    postsRepository = mock(PostsRepository.class);
    heartService = new HeartServiceImpl(heartRepository, memberRepository, postsRepository);
  }

  @Test
  @DisplayName("좋아요 존재 여부 확인")
  void existHeart() {
    when(heartRepository.findByMemberNoAndPostsUrl(anyInt(), anyString()))
            .thenReturn(Optional.empty());

    boolean result = heartService.existHeart(1, "post");

    assertFalse(result);
  }

  @Test
  @DisplayName("존재하는 좋아요")
  void existHeartTrue() {
    Member member = MemberDummy.dummy();
    Blog blog = BlogDummy.dummy(member);
    Category category = CategoryDummy.dummy(blog);
    Posts posts = PostsDummy.dummy(category);
    Heart heart = HeartDummy.dummy(member, posts);

    when(heartRepository.findByMemberNoAndPostsUrl(anyInt(), anyString()))
            .thenReturn(Optional.of(heart));

    boolean result = heartService.existHeart(1, "post");

    assertTrue(result);
  }

  @Test
  @DisplayName("좋아요 수 조회")
  void countHeart() {
    when(heartRepository.getHeartCountsByPost(anyString())).thenReturn(3L);

    Long result = heartService.countHeart("post");

    assertEquals(3L, result);
  }

  @Test
  @DisplayName("좋아요")
  void heart() {
    Member member = MemberDummy.dummy();
    ReflectionTestUtils.setField(member, "memberNo", 1);
    Blog blog = BlogDummy.dummy(member);
    Category category = CategoryDummy.dummy(blog);
    Posts posts = PostsDummy.dummy(category);

    when(memberRepository.findById(anyInt())).thenReturn(Optional.of(member));
    when(postsRepository.findByUrl(anyString())).thenReturn(Optional.of(posts));
    when(heartRepository.findByMemberNoAndPostsUrl(anyInt(), anyString())).thenReturn(Optional.empty());

    heartService.heart(1, "post");

    verify(heartRepository, times(1)).save(any());
  }

  @Test
  @DisplayName("취소했다 다시 좋아요")
  void heartModify() {
    Member member = MemberDummy.dummy();
    ReflectionTestUtils.setField(member, "memberNo", 1);
    Blog blog = BlogDummy.dummy(member);
    Category category = CategoryDummy.dummy(blog);
    Posts posts = PostsDummy.dummy(category);
    Heart heart = HeartDummy.dummy(member, posts);
    ReflectionTestUtils.setField(heart, "deleted", true);

    when(memberRepository.findById(anyInt())).thenReturn(Optional.of(member));
    when(postsRepository.findByUrl(anyString())).thenReturn(Optional.of(posts));
    when(heartRepository.findByMemberNoAndPostsUrl(anyInt(), anyString())).thenReturn(Optional.of(heart));

    heartService.heart(1, "post");

    verify(heartRepository, times(1)).save(any());
    assertFalse(heart.isDeleted());
  }

  @Test
  @DisplayName("좋아요 취소")
  void deleteHeart() {
    Member member = MemberDummy.dummy();
    ReflectionTestUtils.setField(member, "memberNo", 1);
    Blog blog = BlogDummy.dummy(member);
    Category category = CategoryDummy.dummy(blog);
    Posts posts = PostsDummy.dummy(category);
    Heart heart = HeartDummy.dummy(member, posts);

    when(heartRepository.findByMemberNoAndPostsUrl(anyInt(), anyString()))
            .thenReturn(Optional.of(heart));

    heartService.deleteHeart(1, "post");

    assertTrue(heart.isDeleted());
  }
}