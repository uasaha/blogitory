package com.blogitory.blog.mail.controller;

import com.blogitory.blog.mail.dto.MailVerificationRequestDto;
import com.blogitory.blog.mail.dto.MailVerificationResponseDto;
import com.blogitory.blog.mail.service.MailService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Mail Rest api Controller.
 *
 * @author woonseok
 * @since 1.0
 **/
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/mail")
public class MailRestController {
  private final MailService mailService;

  /**
   * Issue new Verification Code.
   *
   * @param email for verifying
   */
  @GetMapping("/verification")
  public void issueVerificationCode(
          @RequestParam @NotBlank @Size(min = 3, max = 100) @Email String email) {
    mailService.sendVerificationCode(email);
  }

  /**
   * Checking Verification Code.
   *
   * @param requestDto for verifying
   * @return is verified, 200
   */
  @PostMapping("/verification")
  public MailVerificationResponseDto checkVerificationCode(
          @RequestBody @Valid MailVerificationRequestDto requestDto) {
    return new MailVerificationResponseDto(mailService.checkVerificationCode(requestDto));
  }
}
