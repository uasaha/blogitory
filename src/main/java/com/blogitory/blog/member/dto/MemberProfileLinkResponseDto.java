package com.blogitory.blog.member.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Member
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
