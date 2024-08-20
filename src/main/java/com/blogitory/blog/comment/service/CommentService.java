package com.blogitory.blog.comment.service;

import com.blogitory.blog.comment.dto.request.CreateCommentRequestDto;
import com.blogitory.blog.comment.dto.response.GetChildCommentResponseDto;
import com.blogitory.blog.comment.dto.response.GetCommentResponseDto;
import com.blogitory.blog.commons.dto.Pages;
import org.springframework.data.domain.Pageable;

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

  /**
   * Get comment by post.
   *
   * @param postUrl  post url
   * @param pageable pageable
   * @return Comments
   */
  Pages<GetCommentResponseDto> getComments(String postUrl, Pageable pageable);

  /**
   * Get Comment by post.
   *
   * @param postUrl  post url
   * @param parentNo parent comment no
   * @param pageable pageable
   * @return Comments
   */
  Pages<GetChildCommentResponseDto> getChildComments(String postUrl,
                                                     Long parentNo,
                                                     Pageable pageable);

  /**
   * Modify comment.
   *
   * @param memberNo  member no
   * @param commentNo comment no
   * @param contents  new contents
   */
  void modifyComment(Integer memberNo, Long commentNo, String contents);

  /**
   * Delete comment.
   *
   * @param memberNo  member no
   * @param commentNo comment no
   */
  void deleteComment(Integer memberNo, Long commentNo);
}
