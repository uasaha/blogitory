package com.blogitory.blog.comment.service;

import com.blogitory.blog.comment.dto.request.CreateCommentRequestDto;

/**
 * Comment Service.
 *
 * @author woonseok
 * @Date 2024-08-07
 * @since 1.0
 **/
public interface CommentService {

  /**
   * Create comment.
   *
   * @param memberNo   member no
   * @param parentNo   parent comment no
   * @param requestDto request
   */
  void createComment(Integer memberNo, Long parentNo, CreateCommentRequestDto requestDto);
}
