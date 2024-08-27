package com.blogitory.blog.posts.repository;

import com.blogitory.blog.posts.dto.response.GetPopularPostResponseDto;
import com.blogitory.blog.posts.dto.response.GetPostForModifyResponseDto;
import com.blogitory.blog.posts.dto.response.GetPostResponseDto;
import com.blogitory.blog.posts.dto.response.GetRecentPostResponseDto;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.NoRepositoryBean;

/**
 * Posts repository custom for using querydsl.
 *
 * @author woonseok
 * @Date 2024-08-03
 * @since 1.0
 **/
@NoRepositoryBean
public interface PostsRepositoryCustom {

  /**
   * Get posts by post url.
   *
   * @param url url
   * @return post
   */
  Optional<GetPostResponseDto> getPostByPostUrl(String url);

  /**
   * Get posts for modify by url.
   *
   * @param memberNo member no
   * @param postUrl  post url
   * @return response dto
   */
  Optional<GetPostForModifyResponseDto> getPostForModifyByUrl(Integer memberNo, String postUrl);

  /**
   * Get recent posts.
   *
   * @param pageable pageable
   * @return recent posts
   */
  Page<GetRecentPostResponseDto> getRecentPosts(Pageable pageable);

  /**
   * Get recent posts by username.
   *
   * @param pageable pageable
   * @param username username
   * @return recent posts
   */
  List<GetRecentPostResponseDto> getRecentPostByUsername(
          Pageable pageable, String username);

  /**
   * Get recent posts by blog url.
   *
   * @param pageable pageable
   * @param blogUrl  blog url
   * @return recent posts
   */
  Page<GetRecentPostResponseDto> getRecentPostByBlog(
          Pageable pageable, String blogUrl);

  /**
   * Get popular posts by blog.
   *
   * @param blogUrl blog url
   * @return posts list
   */
  List<GetPopularPostResponseDto> getPopularPostsByBlog(String blogUrl);
}
