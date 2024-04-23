package com.blogitory.blog.jwt.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Dto of Member Info.
 *
 * @author woonseok
 * @since 1.0
 **/
@Getter
@AllArgsConstructor
public class MemberInfoDto {
  private Integer memberNo;
  private String email;
  private String name;
  private String refreshToken;
}
