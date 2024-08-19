package com.blogitory.blog.comment.repository;

import com.blogitory.blog.comment.dto.response.GetChildCommentResponseDto;
import com.blogitory.blog.comment.dto.response.GetCommentResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.NoRepositoryBean;

/**
 * Custom Comment repository for querydsl.
 *
 * @author woonseok
 * @Date 2024-08-13
 * @since 1.0
 **/
@NoRepositoryBean
public interface CommentRepositoryCustom {
  /**
   * Get Comments by posts.
   *
   * @param postsUrl post url
   * @param pageable pageable
   * @return Comments
   */
  Page<GetCommentResponseDto> getComments(String postsUrl, Pageable pageable);

  /**
   * Get Comments by posts.
   *
   * @param postsUrl post url
   * @param parentNo parent comment no
   * @param pageable pageable
   * @return Comments
   */
  Page<GetChildCommentResponseDto> getChildCommentsByParent(String postsUrl,
                                                            Long parentNo,
                                                            Pageable pageable);
}
