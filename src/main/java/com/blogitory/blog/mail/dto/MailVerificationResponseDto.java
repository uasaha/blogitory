package com.blogitory.blog.mail.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Dto of is verified.
 *
 * @author woonseok
 * @since 1.0
 **/
@Getter
@AllArgsConstructor
public class MailVerificationResponseDto {
  private final boolean isVerified;
}
