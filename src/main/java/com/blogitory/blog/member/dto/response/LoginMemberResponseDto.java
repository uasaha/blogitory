package com.blogitory.blog.member.dto.response;

import java.util.List;
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
public class LoginMemberResponseDto {
  private Integer memberNo;
  private String email;
  private String username;
  private String name;
  private String pfp;
  private String password;
  private List<String> roles;
}
