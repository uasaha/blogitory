package com.blogitory.blog.mail.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 설명 작성 필!
 *
 * @author woonseok
 * @since 1.0
 **/
@Getter
@AllArgsConstructor
public class MailVerificationResponseDto {
  private final boolean isVerified;
}
