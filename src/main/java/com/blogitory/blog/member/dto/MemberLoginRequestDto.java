package com.blogitory.blog.member.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Dto for login request.
 *
 * @author woonseok
 * @since 1.0
 **/
@Getter
@AllArgsConstructor
public class MemberLoginRequestDto {
  private String email;
  private String password;
}
