package com.blogitory.blog.member.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
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
  @Email
  private String email;

  @Size(min = 1, max = 255)
  private String password;
}
