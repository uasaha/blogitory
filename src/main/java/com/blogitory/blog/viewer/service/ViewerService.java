package com.blogitory.blog.viewer.service;

import com.blogitory.blog.viewer.dto.GetViewerCountResponseDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import java.util.List;

/**
 * Viewer service.
 *
 * @author woonseok
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
  Integer getViewersCount(Integer memberNo, String postsUrl);

  /**
   * Save count to DB.
   *
   * @throws JsonProcessingException Object mapper exception
   */
  void persistence() throws JsonProcessingException;

  /**
   * Get viewer monthly count.
   *
   * @param memberNo member no
   * @param postsUrl post url
   * @return monthly count
   */
  List<GetViewerCountResponseDto> getViewerMonthlyCount(Integer memberNo, String postsUrl);
}
