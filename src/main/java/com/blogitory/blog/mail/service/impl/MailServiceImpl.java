package com.blogitory.blog.mail.service.impl;

import com.blogitory.blog.mail.dto.MailVerificationRequestDto;
import com.blogitory.blog.mail.exception.EmailNotVerificationException;
import com.blogitory.blog.mail.service.MailService;
import com.blogitory.blog.member.exception.MemberEmailAlreadyUsedException;
import com.blogitory.blog.member.service.MemberService;
import java.util.Objects;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

/**
 * An Implementation of MailService.
 *
 * @author woonseok
 * @since 1.0
 **/
@Service
@RequiredArgsConstructor
public class MailServiceImpl implements MailService {
  private static final String MAIL_SUBJECT = "Blogitory verification code: ";
  private static final Integer MAIL_TIMEOUT = 10;

  private final MemberService memberService;
  private final JavaMailSender javaMailSender;
  private final RedisTemplate<String, Object> redisTemplate;
  private final Random random = new Random();

  /**
   * {@inheritDoc}
   */
  @Override
  public void sendVerificationCode(String email) {
    if (memberService.existMemberByEmail(email)) {
      throw new MemberEmailAlreadyUsedException(email);
    }

    String verificationCode = getNewVerificationCode();
    redisTemplate.opsForValue().set(email, verificationCode, MAIL_TIMEOUT, TimeUnit.MINUTES);

    SimpleMailMessage mailMessage = new SimpleMailMessage();
    mailMessage.setTo(email);
    mailMessage.setSubject(MAIL_SUBJECT + verificationCode);
    mailMessage.setText(MAIL_SUBJECT + verificationCode);

    javaMailSender.send(mailMessage);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean checkVerificationCode(MailVerificationRequestDto requestDto) {
    String verificationCode = Objects.requireNonNull(
            redisTemplate.opsForValue().get(requestDto.getEmail())).toString();

    redisTemplate.delete(requestDto.getEmail());

    if (!verificationCode.equals(requestDto.getVerificationCode())) {
      throw new EmailNotVerificationException(requestDto.getEmail());
    }

    return true;
  }

  /**
   * Get new Verification Code.
   *
   * @return Verification Code
   */
  private String getNewVerificationCode() {
    return String.valueOf(random.nextInt(888_888) + 100_000);
  }
}
