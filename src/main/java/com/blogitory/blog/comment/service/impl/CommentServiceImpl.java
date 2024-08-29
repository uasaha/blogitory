package com.blogitory.blog.comment.service.impl;

import com.blogitory.blog.comment.dto.request.CreateCommentRequestDto;
import com.blogitory.blog.comment.dto.response.GetChildCommentResponseDto;
import com.blogitory.blog.comment.dto.response.GetCommentResponseDto;
import com.blogitory.blog.comment.dto.response.GetLatestCommentListResponseDto;
import com.blogitory.blog.comment.entity.Comment;
import com.blogitory.blog.comment.repository.CommentRepository;
import com.blogitory.blog.comment.service.CommentService;
import com.blogitory.blog.commons.dto.Pages;
import com.blogitory.blog.commons.exception.NotFoundException;
import com.blogitory.blog.member.entity.Member;
import com.blogitory.blog.member.repository.MemberRepository;
import com.blogitory.blog.posts.entity.Posts;
import com.blogitory.blog.posts.repository.PostsRepository;
import com.blogitory.blog.security.exception.AuthorizationException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementation of Comment service.
 *
 * @author woonseok
 * @Date 2024-08-07
 * @since 1.0
 **/
@Service
@Transactional
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
  private final MemberRepository memberRepository;
  private final CommentRepository commentRepository;
  private final PostsRepository postsRepository;

  /**
   * {@inheritDoc}
   */
  @Override
  public void createComment(Integer memberNo, Long parentNo, CreateCommentRequestDto requestDto) {
    Member member = memberRepository.findById(memberNo)
            .orElseThrow(() -> new NotFoundException(Member.class));
    Posts posts = postsRepository.findByUrl(requestDto.getPostsUrl())
            .orElseThrow(() -> new NotFoundException(Posts.class));

    Comment comment = new Comment(member, posts, requestDto.getContent());

    if (Objects.nonNull(parentNo)) {
      Optional<Comment> parent = commentRepository.findById(parentNo);

      if (parent.isEmpty() || parent.get().isDeleted()) {
        throw new NotFoundException(Comment.class);
      }

      comment.parent(parent.get());
    }

    commentRepository.save(comment);
  }

  /**
   * {@inheritDoc}
   */
  @Transactional(readOnly = true)
  @Override
  public Pages<GetCommentResponseDto> getComments(String postUrl, Pageable pageable) {
    Page<GetCommentResponseDto> page = commentRepository.getComments(postUrl, pageable);

    List<GetCommentResponseDto> result = new ArrayList<>();

    page.getContent().forEach(comment -> {
      if (comment.isDeleted()) {
        comment = new GetCommentResponseDto(
                null, null, null,
                comment.getCommentNo(), null, true,
                null, null, comment.getChildCnt());
      }

      result.add(comment);
    });

    return new Pages<>(result,
            pageable.getPageNumber(),
            page.hasPrevious(),
            page.hasNext(),
            page.getTotalElements());
  }

  /**
   * {@inheritDoc}
   */
  @Transactional(readOnly = true)
  @Override
  public Long getCommentCountByPost(String postUrl) {
    return commentRepository.getCommentCountByPost(postUrl);
  }

  /**
   * {@inheritDoc}
   */
  @Transactional(readOnly = true)
  @Override
  public Pages<GetChildCommentResponseDto> getChildComments(String postUrl,
                                                       Long parentNo,
                                                       Pageable pageable) {
    Page<GetChildCommentResponseDto> page =
            commentRepository.getChildCommentsByParent(postUrl, parentNo, pageable);

    List<GetChildCommentResponseDto> result = new ArrayList<>();

    page.getContent().forEach(comment -> {
      if (comment.isDeleted()) {
        comment = new GetChildCommentResponseDto(
                null, null, null,
                comment.getParentNo(), comment.getCommentNo(), null,
                true, null, null);
      }

      result.add(comment);
    });

    return new Pages<>(result,
            pageable.getPageNumber(),
            page.hasPrevious(),
            page.hasNext(),
            page.getTotalElements());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void modifyComment(Integer memberNo, Long commentNo, String contents) {
    Member member = memberRepository.findById(memberNo)
            .orElseThrow(() -> new NotFoundException(Member.class));

    Comment comment = commentRepository.findById(commentNo)
            .orElseThrow(() -> new NotFoundException(Comment.class));

    if (!comment.getMember().getMemberNo().equals(member.getMemberNo())) {
      throw new AuthorizationException();
    }

    comment.modify(contents);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void deleteComment(Integer memberNo, Long commentNo) {
    Member member = memberRepository.findById(memberNo)
            .orElseThrow(() -> new NotFoundException(Member.class));

    Comment comment = commentRepository.findById(commentNo)
            .orElseThrow(() -> new NotFoundException(Comment.class));

    if (!comment.getMember().getMemberNo().equals(member.getMemberNo())) {
      throw new AuthorizationException();
    }

    comment.delete();
  }

  /**
   * {@inheritDoc}
   */
  @Transactional(readOnly = true)
  @Override
  public List<GetLatestCommentListResponseDto> getRecentComments(String username, String blogUrl) {
    return commentRepository.getRecentCommentsByBlog(username, blogUrl);
  }
}