package com.blogitory.blog.image.service;

import com.blogitory.blog.image.dto.ThumbnailUpdateResponseDto;
import org.springframework.web.multipart.MultipartFile;

/**
 * Image Service.
 *
 * @author woonseok
 * @since 1.0
 **/
public interface ImageService {

  /**
   * Update user's thumbnail image.
   *
   * @param memberNo user's number
   * @param file     new one
   * @return origin name & url
   */
  ThumbnailUpdateResponseDto uploadThumbnail(Integer memberNo, MultipartFile file);

  /**
   * Delete thumbnail.
   *
   * @param memberNo member no
   */
  void removeThumbnail(Integer memberNo);

  /**
   * Modify blog thumbnail.
   *
   * @param memberNo member no
   * @param blogUrl  blog url
   * @param file     new file
   * @return responses
   */
  ThumbnailUpdateResponseDto updateBlogThumbnail(
          Integer memberNo, String blogUrl, MultipartFile file);

  /**
   * Delete blog thumbnail.
   *
   * @param memberNo member no
   * @param blogUrl  blog url
   */
  void removeBlogThumbnail(Integer memberNo, String blogUrl);
}
