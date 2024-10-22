package com.blogitory.blog.tag.dto;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * GetTagListResponseDto.
 *
 * @author woonseok
 * @since 1.0
 **/
@NoArgsConstructor
@AllArgsConstructor
@Getter
@EqualsAndHashCode
public class GetTagResponseDto {
  private String tagName;
}
