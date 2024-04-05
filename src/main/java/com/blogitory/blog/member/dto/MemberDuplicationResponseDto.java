package com.blogitory.blog.member.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Response Dto for new member's info is duplicated.
 *
 * @author woonseok
 * @since 1.0
 **/
@Getter
@AllArgsConstructor
public class MemberDuplicationResponseDto {
  private boolean isDuplicated;
}
