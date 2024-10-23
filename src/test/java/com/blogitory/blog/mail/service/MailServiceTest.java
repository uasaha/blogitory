package com.blogitory.blog.mail.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.blogitory.blog.commons.exception.NotFoundException;
import com.blogitory.blog.mail.dto.request.GetMailVerificationRequestDto;
import com.blogitory.blog.mail.exception.EmailNotVerificationException;
import com.blogitory.blog.mail.service.impl.MailServiceImpl;
import com.blogitory.blog.member.entity.Member;
import com.blogitory.blog.member.entity.MemberDummy;
import com.blogitory.blog.member.exception.MemberEmailAlreadyUsedException;
import com.blogitory.blog.member.repository.MemberRepository;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessagePreparator;

/**
 * Mail service test.
 *
 * @author woonseok
 * @since 1.0
 */
class MailServiceTest {
  /**
   * The Mail service.
   */
  MailService mailService;
  /**
   * The Member service.
   */
  MemberRepository memberRepository;
  /**
   * The Java mail sender.
   */
  JavaMailSender javaMailSender;
  /**
   * The Redis template.
   */
  RedisTemplate redisTemplate;


  /**
   * Sets up.
   */
  @BeforeEach
  void setUp() {
    memberRepository = mock(MemberRepository.class);
    javaMailSender = mock(JavaMailSender.class);
    redisTemplate = mock(RedisTemplate.class);
    mailService = new MailServiceImpl(memberRepository, javaMailSender, redisTemplate);
  }

  /**
   * Send verification code.
   */
  @Test
  @DisplayName("메일 발송 성공")
  void sendVerificationCode() {
    String email = "test@email.com";

    Member member = MemberDummy.dummy();

    when(memberRepository.findByEmail(any())).thenReturn(Optional.of(member));
    ValueOperations<String, Object> operations = mock(ValueOperations.class);
    when(redisTemplate.opsForValue()).thenReturn(operations);
    doNothing().when(operations).set(anyString(), anyString(), anyLong(), any());

    mailService.sendVerificationCode(email);

    verify(javaMailSender, times(1)).send(any(MimeMessagePreparator.class));
  }

  /**
   * Send verification code failed.
   */
  @Test
  @DisplayName("메일 발송 실패 - 중복 이메일")
  void sendVerificationCodeFailed() {
    when(memberRepository.findByEmail(any())).thenReturn(Optional.empty());

    assertThrows(MemberEmailAlreadyUsedException.class,
            () -> mailService.sendVerificationCode("email"));
  }

  /**
   * Check verification code.
   */
  @Test
  @DisplayName("인증번호 확인")
  void checkVerificationCode() {
    String verificationCode = "123123";
    GetMailVerificationRequestDto requestDto =
            new GetMailVerificationRequestDto("test@email.com", verificationCode);

    ValueOperations<String, Object> valueOperations = mock(ValueOperations.class);
    when(redisTemplate.opsForValue()).thenReturn(valueOperations);
    when(valueOperations.get(any())).thenReturn(verificationCode);
    when(redisTemplate.delete(any())).thenReturn(1L);

    assertTrue(mailService.checkVerificationCode(requestDto));
  }

  /**
   * Check verification code failed.
   */
  @Test
  @DisplayName("인증번호 확인 실패 - 인증번호 틀림")
  void checkVerificationCodeFailed() {
    String verificationCode = "123123";
    GetMailVerificationRequestDto requestDto =
            new GetMailVerificationRequestDto("test@email.com", "321321");

    ValueOperations<String, Object> valueOperations = mock(ValueOperations.class);
    when(redisTemplate.opsForValue()).thenReturn(valueOperations);
    when(valueOperations.get(any())).thenReturn(verificationCode);
    when(redisTemplate.delete(any())).thenReturn(1L);

    assertThrows(EmailNotVerificationException.class, () -> mailService.checkVerificationCode(requestDto));
  }

  @Test
  @DisplayName("비밀번호 변경 메일 발송 성공")
  void sendPasswordChangeLink() {
    String email = "test@email.com";

    Member member = MemberDummy.dummy();

    when(memberRepository.findByEmail(any())).thenReturn(Optional.of(member));
    ValueOperations<String, Object> operations = mock(ValueOperations.class);
    when(redisTemplate.opsForValue()).thenReturn(operations);
    doNothing().when(operations).set(anyString(), anyString(), anyLong(), any());

    mailService.sendUpdatePassword(email);

    verify(javaMailSender, times(1)).send(any(MimeMessagePreparator.class));
  }

  @Test
  @DisplayName("비밀번호 변경 메일 발송 실패 - 없는 이메일")
  void sendPasswordChangeLinkFailed() {
    String email = "test@email.com";

    when(memberRepository.findByEmail(any())).thenReturn(Optional.empty());
    ValueOperations<String, Object> operations = mock(ValueOperations.class);
    when(redisTemplate.opsForValue()).thenReturn(operations);
    doNothing().when(operations).set(anyString(), anyString(), anyLong(), any());

    assertThrows(NotFoundException.class, () -> mailService.sendUpdatePassword(email));
  }
}