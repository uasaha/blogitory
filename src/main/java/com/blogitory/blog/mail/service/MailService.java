package com.blogitory.blog.mail.service;

import com.blogitory.blog.mail.dto.MailVerificationRequestDto;

/**
 * Service of Mail.
 *
 * @author woonseok
 * @since 1.0
 **/
public interface MailService {
  /**
   * Send email to user verification code.
   *
   * @param email user email
   */
  void sendVerificationCode(String email);

  /**
   * checking verification code.
   *
   * @param requestDto email and verification code
   * @return is verified
   */
  boolean checkVerificationCode(MailVerificationRequestDto requestDto);
}
