package com.blogitory.blog.member.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Member quit request dto.
 *
 * @author woonseok
 * @since 1.0
 **/
@NoArgsConstructor
@Getter
public class LeftMemberRequestDto {
  private String password;
}
