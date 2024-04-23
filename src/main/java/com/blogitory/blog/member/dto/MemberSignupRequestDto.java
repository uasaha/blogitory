package com.blogitory.blog.member.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Dto class for signup member.
 *
 * @author woonseok
 * @since 1.0
 **/
@Getter
@AllArgsConstructor
public class MemberSignupRequestDto {
  @NotBlank
  @Size(min = 2, max = 20, message = "Name must be between 2 and 20 characters")
  @Pattern(regexp = "^[a-zA-Zㄱ-힣0-9]*$")
  private String name;
  @Email
  private String email;
  @NotBlank
  @Size(min = 8, message = "Password must be between 8 and 100 characters")
  private String pwd;
}
