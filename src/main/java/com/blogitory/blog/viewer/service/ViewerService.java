package com.blogitory.blog.viewer.service;

import com.fasterxml.jackson.core.JsonProcessingException;

/**
 * Viewer service.
 *
 * @author woonseok
 * @Date 2024-09-09
 * @since 1.0
 **/
public interface ViewerService {

  /**
   * Save history when view posts.
   *
   * @param postsUrl posts url
   * @param memberNo member no
   * @param ip       client ip
   */
  void viewPosts(String postsUrl, Integer memberNo, String ip);

  /**
   * Get viewers total count.
   *
   * @param postsUrl post url
   * @return count
   */
  Integer getViewersCount(String postsUrl);

  /**
   * Save count to DB.
   *
   * @throws JsonProcessingException Object mapper exception
   */
  void persistence() throws JsonProcessingException;
}
