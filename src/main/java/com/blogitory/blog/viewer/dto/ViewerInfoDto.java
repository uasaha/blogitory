package com.blogitory.blog.viewer.dto;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

/**
 * ViewerInfoDto.
 *
 * @author woonseok
 * @since 1.0
 **/
@EqualsAndHashCode
@Getter
@AllArgsConstructor
public class ViewerInfoDto {
  private Integer memberNo;
  private String ip;
}
