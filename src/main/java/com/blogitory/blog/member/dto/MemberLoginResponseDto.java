package com.blogitory.blog.member.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Dto for login response.
 *
 * @author woonseok
 * @since 1.0
 **/
@Getter
@AllArgsConstructor
public class MemberLoginResponseDto {
  private Integer memberNo;
  private String email;
  private String username;
  private String name;
  private String password;
}
