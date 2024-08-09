package com.blogitory.blog.posts.service;

import com.blogitory.blog.posts.dto.request.SaveTempPostsDto;
import com.blogitory.blog.posts.dto.response.CreatePostsResponseDto;
import com.blogitory.blog.posts.dto.response.GetPostResponseDto;

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
}
