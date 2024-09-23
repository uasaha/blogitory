package com.blogitory.blog.member.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Dto for Signup Oauth member request.
 *
 * @author woonseok
 * @since 1.0
 **/
@Getter
@AllArgsConstructor
public class SignupOauthMemberRequestDto {
  private String provider;
  private String id;
  private String profileThumb;
  @NotBlank
  @Size(min = 1, max = 30, message = "Username must be between 1 and 30 characters long")
  @Pattern(regexp = "^[a-zA-Z0-9-]+$")
  private String username;

  @Size(min = 2, max = 50, message = "Name must be between 2 and 20 characters long")
  @Pattern(regexp = "^[a-zA-Zㄱ-ㅣ가-힣\\d\\s]{2,50}$")
  private String name;
}
