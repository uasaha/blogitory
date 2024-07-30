package com.blogitory.blog.member.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Dto for MemberProfile view.
 *
 * @author woonseok
 * @since 1.0
 **/
@AllArgsConstructor
@Getter
public class MemberProfileLinkResponseDto {
  private Long linkNo;
  private String linkUrl;
}
