package com.blogitory.blog.comment.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.blogitory.blog.blog.entity.Blog;
import com.blogitory.blog.blog.entity.BlogDummy;
import com.blogitory.blog.category.entity.Category;
import com.blogitory.blog.category.entity.CategoryDummy;
import com.blogitory.blog.comment.dto.request.CreateCommentRequestDto;
import com.blogitory.blog.comment.entity.Comment;
import com.blogitory.blog.comment.repository.CommentRepository;
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
import org.mockito.Mockito;
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
}