package com.blogitory.blog.storage.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

/**
 * Dto for File upload response.
 *
 * @author woonseok
 * @since 1.0
 **/
@AllArgsConstructor
@Builder
@Getter
public class UploadFileResponseDto {
  private String url;
  private String originName;
  private String savedName;
  private String extension;
  private String savePath;
}
