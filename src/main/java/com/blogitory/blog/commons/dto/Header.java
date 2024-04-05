package com.blogitory.blog.commons.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Header Dto for handling common response.
 *
 * @author woonseok
 * @since 1.0
 **/
@Getter
@AllArgsConstructor
public class Header {
  private boolean isSuccessful;
  private Integer resultCode;
  private String resultMessage;
}
