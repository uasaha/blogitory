package com.blogitory.blog.comment.service;

import com.blogitory.blog.comment.dto.request.CreateCommentRequestDto;
import com.blogitory.blog.comment.entity.Comment;
import com.blogitory.blog.comment.repository.CommentRepository;
import com.blogitory.blog.commons.exception.NotFoundException;
import com.blogitory.blog.member.entity.Member;
import com.blogitory.blog.member.repository.MemberRepository;
import com.blogitory.blog.posts.entity.Posts;
import com.blogitory.blog.posts.repository.PostsRepository;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
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
      Comment parent = commentRepository.findById(parentNo)
              .orElseThrow(() -> new NotFoundException(Posts.class));

      comment.parent(parent);
    }

    commentRepository.save(comment);
  }
}
