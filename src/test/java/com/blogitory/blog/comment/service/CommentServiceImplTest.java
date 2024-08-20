package com.blogitory.blog.comment.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.blogitory.blog.blog.entity.Blog;
import com.blogitory.blog.blog.entity.BlogDummy;
import com.blogitory.blog.category.entity.Category;
import com.blogitory.blog.category.entity.CategoryDummy;
import com.blogitory.blog.comment.dto.request.CreateCommentRequestDto;
import com.blogitory.blog.comment.dto.response.GetChildCommentResponseDto;
import com.blogitory.blog.comment.dto.response.GetCommentResponseDto;
import com.blogitory.blog.comment.entity.Comment;
import com.blogitory.blog.comment.entity.CommentDummy;
import com.blogitory.blog.comment.repository.CommentRepository;
import com.blogitory.blog.commons.dto.Pages;
import com.blogitory.blog.commons.exception.NotFoundException;
import com.blogitory.blog.member.entity.Member;
import com.blogitory.blog.member.entity.MemberDummy;
import com.blogitory.blog.member.repository.MemberRepository;
import com.blogitory.blog.posts.entity.Posts;
import com.blogitory.blog.posts.entity.PostsDummy;
import com.blogitory.blog.posts.repository.PostsRepository;
import com.blogitory.blog.security.exception.AuthorizationException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.util.ReflectionTestUtils;

/**
 * Comment service test.
 *
 * @author woonseok
 * @Date 2024-08-08
 * @since 1.0
 **/
class CommentServiceImplTest {

  MemberRepository memberRepository;

  CommentRepository commentRepository;

  PostsRepository postsRepository;

  CommentService commentService;

  @BeforeEach
  void setUp() {
    memberRepository = Mockito.mock(MemberRepository.class);
    commentRepository = Mockito.mock(CommentRepository.class);
    postsRepository = Mockito.mock(PostsRepository.class);

    commentService = new CommentServiceImpl(memberRepository,
            commentRepository,
            postsRepository);
  }

  @Test
  @DisplayName("댓글 등록")
  void createComment() {
    Member member = MemberDummy.dummy();
    Blog blog = BlogDummy.dummy(member);
    Category category = CategoryDummy.dummy(blog);
    Posts posts = PostsDummy.dummy(category);

    CreateCommentRequestDto requestDto = new CreateCommentRequestDto();
    ReflectionTestUtils.setField(requestDto, "postsUrl", "url");
    ReflectionTestUtils.setField(requestDto, "content", "content");

    when(memberRepository.findById(any())).thenReturn(Optional.of(member));
    when(postsRepository.findByUrl(any())).thenReturn(Optional.of(posts));

    commentService.createComment(member.getMemberNo(), null, requestDto);

    verify(commentRepository, times(1))
            .save(any(Comment.class));
  }

  @Test
  @DisplayName("자식 댓글 등록")
  void createChildComment() {
    Member member = MemberDummy.dummy();
    Blog blog = BlogDummy.dummy(member);
    Category category = CategoryDummy.dummy(blog);
    Posts posts = PostsDummy.dummy(category);

    CreateCommentRequestDto requestDto = new CreateCommentRequestDto();
    ReflectionTestUtils.setField(requestDto, "postsUrl", "url");
    ReflectionTestUtils.setField(requestDto, "content", "content");

    Comment comment = new Comment(member, posts, requestDto.getContent());

    when(memberRepository.findById(any())).thenReturn(Optional.of(member));
    when(postsRepository.findByUrl(any())).thenReturn(Optional.of(posts));
    when(commentRepository.findById(any())).thenReturn(Optional.of(comment));

    commentService.createComment(member.getMemberNo(), 1L, requestDto);

    verify(commentRepository, times(1))
            .save(any(Comment.class));
  }

  @Test
  @DisplayName("자식 댓글 등록 실패 - 부모없음")
  void createChildCommentFailed() {
    Member member = MemberDummy.dummy();
    Blog blog = BlogDummy.dummy(member);
    Category category = CategoryDummy.dummy(blog);
    Posts posts = PostsDummy.dummy(category);

    CreateCommentRequestDto requestDto = new CreateCommentRequestDto();
    ReflectionTestUtils.setField(requestDto, "postsUrl", "url");
    ReflectionTestUtils.setField(requestDto, "content", "content");

    Comment comment = new Comment(member, posts, requestDto.getContent());
    comment.delete();

    when(memberRepository.findById(any())).thenReturn(Optional.of(member));
    when(postsRepository.findByUrl(any())).thenReturn(Optional.of(posts));
    when(commentRepository.findById(any())).thenReturn(Optional.of(comment));

    assertThrows(NotFoundException.class,
            () -> commentService.createComment(1, 1L, requestDto));
  }

  @Test
  @DisplayName("댓글 조회")
  void getComments() {
    GetCommentResponseDto responseDto = new GetCommentResponseDto(
            "name", "username", "userpfp",
            1L, "content", false,
            LocalDateTime.now(), null, 0L);
    Pageable pageable = PageRequest.of(0, 10);
    GetCommentResponseDto responseDto2 = new GetCommentResponseDto(
            "name", "username", "userpfp",
            2L, "content", true,
            LocalDateTime.now(), null, 0L);

    Page<GetCommentResponseDto> page =
            new PageImpl<>(List.of(responseDto, responseDto2), pageable, 1L);

    when(commentRepository.getComments(anyString(), any()))
            .thenReturn(page);

    Pages<GetCommentResponseDto> pages = commentService.getComments("postUrl", pageable);

    GetCommentResponseDto result = pages.body().getFirst();
    GetCommentResponseDto deletedResult = pages.body().getLast();

    assertEquals(responseDto.getName(), result.getName());
    assertEquals(responseDto.getUsername(), result.getUsername());
    assertEquals(responseDto.getContent(), result.getContent());
    assertEquals(responseDto.getUserPfp(), result.getUserPfp());
    assertNull(deletedResult.getName());
    assertNull(deletedResult.getUsername());
  }

  @Test
  @DisplayName("답글 조회")
  void getChildComments() {
    GetChildCommentResponseDto responseDto = new GetChildCommentResponseDto(
            "name", "username", "pfp", 1L, 2L,
            "contents", false, LocalDateTime.now(), null);

    Pageable pageable = PageRequest.of(0, 10);
    GetChildCommentResponseDto responseDto2 = new GetChildCommentResponseDto(
            "name", "username", "pfp", 1L, 2L,
            "contents", true, LocalDateTime.now(), null);

    Page<GetChildCommentResponseDto> page =
            new PageImpl<>(List.of(responseDto, responseDto2), pageable, 1L);

    when(commentRepository.getChildCommentsByParent(anyString(), any(), any()))
            .thenReturn(page);

    Pages<GetChildCommentResponseDto> pages = commentService.getChildComments("postUrl", 1L, pageable);

    GetChildCommentResponseDto result = pages.body().getFirst();
    GetChildCommentResponseDto deletedResult = pages.body().getLast();

    assertEquals(responseDto.getName(), result.getName());
    assertEquals(responseDto.getUsername(), result.getUsername());
    assertEquals(responseDto.getContent(), result.getContent());
    assertEquals(responseDto.getUserPfp(), result.getUserPfp());
    assertNull(deletedResult.getName());
    assertNull(deletedResult.getUsername());
  }

  @Test
  @DisplayName("댓글 수정")
  void modifyComment() {
    Member member = MemberDummy.dummy();
    Blog blog = BlogDummy.dummy(member);
    Category category = CategoryDummy.dummy(blog);
    Posts posts = PostsDummy.dummy(category);
    Comment comment = CommentDummy.dummy(member, posts);
    String newContents = "newContents";

    when(memberRepository.findById(any())).thenReturn(Optional.of(member));
    when(commentRepository.findById(any())).thenReturn(Optional.of(comment));

    commentService.modifyComment(member.getMemberNo(), comment.getCommentNo(), newContents);

    assertEquals(comment.getContents(), newContents);
  }

  @Test
  @DisplayName("댓글 수정 실패 - 다른 유저")
  void modifyCommentFailed() {
    Member member = MemberDummy.dummy();
    Blog blog = BlogDummy.dummy(member);
    Category category = CategoryDummy.dummy(blog);
    Posts posts = PostsDummy.dummy(category);
    Comment comment = CommentDummy.dummy(member, posts);
    String newContents = "newContents";

    Member otherMember = MemberDummy.dummy();
    ReflectionTestUtils.setField(otherMember, "memberNo", 2);

    when(memberRepository.findById(any())).thenReturn(Optional.of(otherMember));
    when(commentRepository.findById(any())).thenReturn(Optional.of(comment));

    assertThrows(AuthorizationException.class, () ->
            commentService.modifyComment(1, 1L, newContents));
  }

  @Test
  @DisplayName("댓글 삭제")
  void deleteComment() {
    Member member = MemberDummy.dummy();
    Blog blog = BlogDummy.dummy(member);
    Category category = CategoryDummy.dummy(blog);
    Posts posts = PostsDummy.dummy(category);
    Comment comment = CommentDummy.dummy(member, posts);

    when(memberRepository.findById(any())).thenReturn(Optional.of(member));
    when(commentRepository.findById(any())).thenReturn(Optional.of(comment));

    commentService.deleteComment(member.getMemberNo(), comment.getCommentNo());

    assertTrue(comment.isDeleted());
  }

  @Test
  @DisplayName("댓글 삭제 실패 - 다른 유저")
  void deleteCommentFailed() {
    Member member = MemberDummy.dummy();
    Blog blog = BlogDummy.dummy(member);
    Category category = CategoryDummy.dummy(blog);
    Posts posts = PostsDummy.dummy(category);
    Comment comment = CommentDummy.dummy(member, posts);

    Member otherMember = MemberDummy.dummy();
    ReflectionTestUtils.setField(otherMember, "memberNo", 2);

    when(memberRepository.findById(any())).thenReturn(Optional.of(otherMember));
    when(commentRepository.findById(any())).thenReturn(Optional.of(comment));

    assertThrows(AuthorizationException.class, () ->
            commentService.deleteComment(1, 1L));
  }
}