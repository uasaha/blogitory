package com.blogitory.blog.member.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Dto for login request.
 *
 * @author woonseok
 * @since 1.0
 **/
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MemberLoginRequestDto {
  private String email;
  private String password;
}
