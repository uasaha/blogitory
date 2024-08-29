package com.blogitory.blog.heart.service;

/**
 * Heart service.
 *
 * @author woonseok
 * @Date 2024-08-29
 * @since 1.0
 **/
public interface HeartService {
  /**
   * Exist heart.
   *
   * @param memberNo member no
   * @param postKey  post url
   * @return exists
   */
  boolean existHeart(Integer memberNo, String postKey);

  /**
   * Counts of hearts.
   *
   * @param postKey post url
   * @return count
   */
  Long countHeart(String postKey);

  /**
   * Do heart.
   *
   * @param memberNo member no
   * @param postKey  post url
   */
  void heart(Integer memberNo, String postKey);

  /**
   * Delete heart.
   *
   * @param memberNo member no
   * @param postKey  post url
   */
  void deleteHeart(Integer memberNo, String postKey);
}
