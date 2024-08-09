package com.blogitory.blog.mail.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Dto for checking verification code of email.
 *
 * @author woonseok
 * @since 1.0
 **/
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class GetMailVerificationRequestDto {
  @NotBlank
  @Email
  private String email;
  @NotBlank
  @Size(min = 6, max = 6)
  private String verificationCode;
}
