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
  ThumbnailUpdateResponseDto updateThumbnail(Integer memberNo, MultipartFile file);
}
