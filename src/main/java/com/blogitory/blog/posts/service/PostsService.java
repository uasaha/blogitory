package com.blogitory.blog.posts.service;

import com.blogitory.blog.commons.dto.Pages;
import com.blogitory.blog.posts.dto.request.ModifyPostsRequestDto;
import com.blogitory.blog.posts.dto.request.SaveTempPostsDto;
import com.blogitory.blog.posts.dto.response.CreatePostsResponseDto;
import com.blogitory.blog.posts.dto.response.GetPopularPostResponseDto;
import com.blogitory.blog.posts.dto.response.GetPostActivityResponseDto;
import com.blogitory.blog.posts.dto.response.GetPostForModifyResponseDto;
import com.blogitory.blog.posts.dto.response.GetPostManageResponseDto;
import com.blogitory.blog.posts.dto.response.GetPostResponseDto;
import com.blogitory.blog.posts.dto.response.GetRecentPostResponseDto;
import java.time.DayOfWeek;
import java.util.List;
import java.util.Map;
import org.springframework.data.domain.Pageable;

/**
 * Posts service.
 *
 * @author woonseok
 * @Date 2024-08-01
 * @since 1.0
 **/
public interface PostsService {

  /**
   * Get temp posts id.
   *
   * @param memberNo member no
   * @return temp posts id
   */
  String getTempPostsId(Integer memberNo);

  /**
   * Load temp posts.
   *
   * @param id       temp post id
   * @param memberNo member no
   * @return temp posts
   */
  SaveTempPostsDto loadTempPosts(String id, Integer memberNo);

  /**
   * Save temp posts.
   *
   * @param id       temp post id
   * @param saveDto  request dto
   * @param memberNo member no
   */
  void saveTempPosts(String id, SaveTempPostsDto saveDto, Integer memberNo);

  /**
   * Create posts.
   *
   * @param tp       temp post id
   * @param memberNo member no
   * @param saveDto  save dto
   * @return saved post url
   */
  CreatePostsResponseDto createPosts(String tp, Integer memberNo, SaveTempPostsDto saveDto);

  /**
   * Delete temp posts.
   *
   * @param memberNo member no
   * @param tp       temp post id
   */
  void deleteTempPosts(Integer memberNo, String tp);

  /**
   * Get post by url.
   *
   * @param postUrl post url
   * @return post
   */
  GetPostResponseDto getPostByUrl(String postUrl);

  /**
   * Get post for modifying.
   *
   * @param memberNo current member no
   * @param postUrl  post url
   * @return response dto
   */
  GetPostForModifyResponseDto getPostForModifyByUrl(Integer memberNo, String postUrl);

  /**
   * Modify post.
   *
   * @param memberNo   member no
   * @param postKey    post key
   * @param requestDto requests
   */
  void modifyPosts(Integer memberNo, String postKey, ModifyPostsRequestDto requestDto);

  /**
   * Delete post.
   *
   * @param memberNo member no
   * @param postKey  post key
   */
  void deletePosts(Integer memberNo, String postKey);

  /**
   * Get recent posts.
   *
   * @param pageable pageable
   * @return recent posts
   */
  Pages<GetRecentPostResponseDto> getRecentPost(Pageable pageable);

  /**
   * Get recent posts by username.
   *
   * @param username username
   * @return recent posts
   */
  List<GetRecentPostResponseDto> getRecentPostByUsername(String username);

  /**
   * Get recent posts by blog url.
   *
   * @param pageable pageable
   * @param blogUrl  blog url
   * @return recent posts
   */
  Pages<GetRecentPostResponseDto> getRecentPostByBlog(Pageable pageable, String blogUrl);

  /**
   * Get recent posts by category.
   *
   * @param pageable     pageable
   * @param blogUrl      blog url
   * @param categoryName category name
   * @return recent post
   */
  Pages<GetRecentPostResponseDto> getRecentPostByCategory(Pageable pageable,
                                                          String blogUrl,
                                                          String categoryName);

  /**
   * Get recent post by tag.
   *
   * @param pageable pageable
   * @param blogUrl  blog url
   * @param tagName  tag name
   * @return recent post
   */
  Pages<GetRecentPostResponseDto> getRecentPostByTag(Pageable pageable,
                                                     String blogUrl,
                                                     String tagName);

  /**
   * Get popular posts by blog.
   *
   * @param blogUrl blog url
   * @return posts list
   */
  List<GetPopularPostResponseDto> getPopularPostsByBlog(String blogUrl);

  /**
   * Close posts.
   *
   * @param memberNo member no
   * @param postKey  post key
   */
  void closePosts(Integer memberNo, String postKey);

  /**
   * Get posts by member no for Settings.
   *
   * @param pageable pageable
   * @param memberNo member no
   * @return posts
   */
  Pages<GetPostManageResponseDto> getPostsByMemberNo(Pageable pageable, Integer memberNo);

  /**
   * Open posts.
   *
   * @param memberNo member no
   * @param postKey  post key
   */
  void openPosts(Integer memberNo, String postKey);

  /**
   * Get posts by hearts.
   *
   * @param memberNo member no
   * @param pageable pageable
   * @return posts
   */
  Pages<GetRecentPostResponseDto> getPostsByHearts(Integer memberNo, Pageable pageable);

  /**
   * Get Post Activity.
   *
   * @param username username
   * @return Activity, grouping by day of week
   */
  Map<DayOfWeek, List<GetPostActivityResponseDto>> getPostActivity(String username);
}
