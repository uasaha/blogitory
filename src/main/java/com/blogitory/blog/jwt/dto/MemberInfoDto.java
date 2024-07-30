package com.blogitory.blog.jwt.dto;

import java.util.List;
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
  private String username;
  private String name;
  private String pfp;
  private List<String> roles;
  private String refreshToken;
}
