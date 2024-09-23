package com.blogitory.blog.comment.repository;

import com.blogitory.blog.comment.dto.response.GetChildCommentResponseDto;
import com.blogitory.blog.comment.dto.response.GetCommentResponseDto;
import com.blogitory.blog.comment.dto.response.GetLatestCommentListResponseDto;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.NoRepositoryBean;

/**
 * Custom Comment repository for querydsl.
 *
 * @author woonseok
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
   * Get comments count.
   *
   * @param postUrl post url
   * @return count
   */
  Long getCommentCountByPost(String postUrl);

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

  /**
   * Get recent comments. (limit 4)
   *
   * @param username blog owner
   * @param blogUrl  post url
   * @return comments
   */
  List<GetLatestCommentListResponseDto> getRecentCommentsByBlog(String username, String blogUrl);
}
