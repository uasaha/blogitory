package com.blogitory.blog.posts.repository;

import com.blogitory.blog.posts.dto.response.GetPopularPostResponseDto;
import com.blogitory.blog.posts.dto.response.GetPostActivityResponseDto;
import com.blogitory.blog.posts.dto.response.GetPostForModifyResponseDto;
import com.blogitory.blog.posts.dto.response.GetPostManageResponseDto;
import com.blogitory.blog.posts.dto.response.GetPostResponseDto;
import com.blogitory.blog.posts.dto.response.GetRecentPostResponseDto;
import java.time.LocalDate;
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
   * Get posts count by blog.
   *
   * @param blogUrl blog url
   * @return count
   */
  long getPostsCountByBlog(String blogUrl);

  /**
   * Get recent posts by category.
   *
   * @param pageable     pageable
   * @param blogUrl      blog url
   * @param categoryName category name
   * @return recent posts
   */
  Page<GetRecentPostResponseDto> getRecentPostByCategory(
          Pageable pageable, String blogUrl, String categoryName);

  /**
   * Get popular posts by blog.
   *
   * @param blogUrl blog url
   * @return posts list
   */
  List<GetPopularPostResponseDto> getPopularPostsByBlog(String blogUrl);

  /**
   * Get recent posts by tag.
   *
   * @param pageable pageable
   * @param blogUrl  blog url
   * @param tagName  tag name
   * @return posts
   */
  Page<GetRecentPostResponseDto> getRecentPostsByTag(Pageable pageable,
                                                     String blogUrl,
                                                     String tagName);

  /**
   * Get posts by member no for settings.
   *
   * @param pageable pageable
   * @param memberNo member no
   * @return posts
   */
  Page<GetPostManageResponseDto> getPostsByMemberNo(Pageable pageable, Integer memberNo);

  /**
   * Get hearts posts.
   *
   * @param memberNo member no
   * @param pageable pageable
   * @return posts
   */
  Page<GetRecentPostResponseDto> getPostsByHearts(Integer memberNo, Pageable pageable);

  /**
   * Get post activity.
   *
   * @param username username
   * @param start    start date
   * @param end      end date
   * @return activities
   */
  List<GetPostActivityResponseDto> getPostActivity(String username,
                                                   LocalDate start,
                                                   LocalDate end);

  /**
   * Search posts.
   *
   * @param pageable pageable
   * @param words    search keyword
   * @return posts
   */
  Page<GetRecentPostResponseDto> searchPosts(Pageable pageable, String words);
}
