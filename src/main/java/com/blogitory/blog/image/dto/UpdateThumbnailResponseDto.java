package com.blogitory.blog.image.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Dto for update Thumbnail.
 *
 * @author woonseok
 * @since 1.0
 **/
@AllArgsConstructor
@Getter
public class UpdateThumbnailResponseDto {
  private String url;
  private String originName;
}
