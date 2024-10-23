package com.blogitory.blog.mail.service.impl;

import com.blogitory.blog.commons.exception.NotFoundException;
import com.blogitory.blog.mail.dto.request.GetMailVerificationRequestDto;
import com.blogitory.blog.mail.exception.EmailNotVerificationException;
import com.blogitory.blog.mail.exception.OauthMemberException;
import com.blogitory.blog.mail.service.MailService;
import com.blogitory.blog.member.entity.Member;
import com.blogitory.blog.member.exception.MemberEmailAlreadyUsedException;
import com.blogitory.blog.member.repository.MemberRepository;
import java.security.SecureRandom;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * An Implementation of MailService.
 *
 * @author woonseok
 * @since 1.0
 **/
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MailServiceImpl implements MailService {
  private static final String VERIFY_MAIL_SUBJECT = "Blogitory - Verification code";
  private static final String PASSWORD_MAIL_SUBJECT = "Blogitory - Password Change";
  private static final Integer MAIL_TIMEOUT = 10;
  private static final String PASSWORD_UUID_PREFIX = "pwd-";

  private final MemberRepository memberRepository;
  private final JavaMailSender javaMailSender;
  private final RedisTemplate<String, Object> redisTemplate;
  private final SecureRandom random = new SecureRandom();

  /**
   * {@inheritDoc}
   */
  @Override
  public void sendVerificationCode(String email) {
    if (memberRepository.findByEmail(email).isEmpty()) {
      throw new MemberEmailAlreadyUsedException(email);
    }

    String verificationCode = getNewVerificationCode();
    redisTemplate.opsForValue().set(email, verificationCode, MAIL_TIMEOUT, TimeUnit.MINUTES);

    MimeMessagePreparator preparator =
            mimeMessagePreparator(email,
                    VERIFY_MAIL_SUBJECT,
                    verificationCodeContent(verificationCode));

    javaMailSender.send(preparator);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean checkVerificationCode(GetMailVerificationRequestDto requestDto) {
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

  /**
   * {@inheritDoc}
   */
  @Override
  public void sendUpdatePassword(String email) {
    Optional<Member> memberOptional = memberRepository.findByEmail(email);

    if (memberOptional.isEmpty()) {
      throw new NotFoundException(Member.class);
    }

    Member member = memberOptional.get();

    if (Objects.nonNull(member.getOauth()) && !member.getOauth().isEmpty()) {
      throw new OauthMemberException();
    }

    String uuid = UUID.randomUUID().toString();
    String passwordId = PASSWORD_UUID_PREFIX + uuid;

    redisTemplate.opsForValue().set(passwordId, email, MAIL_TIMEOUT, TimeUnit.MINUTES);

    MimeMessagePreparator preparator =
            mimeMessagePreparator(email,
                    PASSWORD_MAIL_SUBJECT,
                    updatePasswordContent(passwordId));

    javaMailSender.send(preparator);
  }

  /**
   * Get mime message preparator.
   *
   * @param email   email
   * @param subject subject
   * @param text    text
   * @return MimeMessagePreparator
   */
  private MimeMessagePreparator mimeMessagePreparator(String email,
                                                      String subject,
                                                      String text) {
    return mimeMessage -> {
      MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "UTF-8");
      helper.setTo(email);
      helper.setSubject(subject);
      helper.setText(text, true);
    };
  }

  /**
   * Get verification code content.
   *
   * @param verificationCode verification code
   * @return content
   */
  private static String verificationCodeContent(String verificationCode) {
    return "<!DOCTYPE html><html lang='en'><head><meta charset='UTF-8'><meta name='viewport' "
            + "content='width=device-width, initial-scale=1.0'><title>Verification Code"
            + "</title></head><body style='margin: 0; padding: 0; background-color: #f4f4f4; "
            + "font-family: Arial, sans-serif;'><table role='presentation' style='width: 100%; "
            + "border-collapse: collapse; background-color: #f4f4f4; padding: 20px 0;'><tr>"
            + "<td align='center'><table role='presentation' style='width: 100%; max-width: "
            + "600px; border-collapse: collapse; background-color: #ffffff; border-radius: "
            + "10px; box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);'><tr><td align='center' "
            + "style='padding: 10px 0; border-bottom: 1px solid #e0e0e0; background-color: "
            + "#375582'><span style='font-size: 35px; color: #FFFFFF;'>Blogitory"
            + "</span></td></tr><tr>"
            + "<td align='center' "
            + "style='padding: 30px 20px;'><h1 style='margin: 0; font-size: 24px;'>"
            + "Verification Code</h1><p style='font-size: 16px; color: #333; margin: 20px 0;'>"
            + "하단의 인증번호를 입력해주세요.</p><div style='font-size: 24px; font-weight: bold; color: "
            + "#333; background-color: #f0f0f0; padding: 10px 20px; border-radius: 5px; "
            + "display: inline-block;'>"
            + verificationCode
            + "</div></td></tr><tr><td align='center' style='padding: 10px 20px; border-top: "
            + "1px solid #e0e0e0;'><p style='font-size: 12px; color: #999;'>If you did not "
            + "request this code, please ignore this email.</p></td></tr></table></td></tr>"
            + "</table></body></html>";
  }

  /**
   * Get password initialization mail content.
   *
   * @param uuid uuid
   * @return content
   */
  private static String updatePasswordContent(String uuid) {
    return "<!DOCTYPE html><html lang='en'><head><meta charset='UTF-8'><meta name='viewport' "
            + "content='width=device-width, initial-scale=1.0'><title>Verification Code"
            + "</title></head><body style='margin: 0; padding: 0; background-color: #f4f4f4; "
            + "font-family: Arial, sans-serif;'><table role='presentation' style='width: 100%; "
            + "border-collapse: collapse; background-color: #f4f4f4; padding: 20px 0;'><tr>"
            + "<td align='center'><table role='presentation' style='width: 100%; max-width: "
            + "600px; border-collapse: collapse; background-color: #ffffff; border-radius: "
            + "10px; box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);'><tr><td align='center' "
            + "style='padding: 10px 0; border-bottom: 1px solid #e0e0e0; background-color: "
            + "#375582'><span style='font-size: 35px; color: #FFFFFF;'>Blogitory"
            + "</span></td></tr><tr>"
            + "<td align='center' "
            + "style='padding: 30px 20px;'><h1 style='margin: 0; font-size: 24px;'>"
            + "Change Password</h1><p style='font-size: 16px; color: #333; margin: 20px 0;'>"
            + "하단을 클릭하여 비밀번호를 변경할 수 있습니다.</p><div style='font-size: 15px; color: "
            + "#333; background-color: #f0f0f0; padding: 10px 20px; border-radius: 5px; "
            + "display: inline-block;'><a href='https://blogitory.com/users/passwords?ui="
            + uuid
            + "'>비밀번호 변경</a></div></td></tr><tr><td align='center' style='padding: 10px 20px; "
            + "border-top: "
            + "1px solid #e0e0e0;'><p style='font-size: 12px; color: #999;'>If you did not "
            + "request changing password, please ignore this email.</p></td></tr></table></td></tr>"
            + "</table></body></html>";
  }
}