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

import com.blogitory.blog.mail.dto.MailVerificationRequestDto;
import com.blogitory.blog.mail.exception.EmailNotVerificationException;
import com.blogitory.blog.mail.service.impl.MailServiceImpl;
import com.blogitory.blog.member.exception.MemberEmailAlreadyUsedException;
import com.blogitory.blog.member.service.MemberService;
import java.util.Random;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

/**
 * Mail service test.
 *
 * @author woonseok
 * @since 1.0
 **/
class MailServiceTest {
  MailService mailService;
  MemberService memberService;
  JavaMailSender javaMailSender;
  RedisTemplate redisTemplate;
  Random random;

  @BeforeEach
  void setUp() {
    memberService = mock(MemberService.class);
    javaMailSender = mock(JavaMailSender.class);
    redisTemplate = mock(RedisTemplate.class);
    random = mock(Random.class);
    mailService = new MailServiceImpl(memberService, javaMailSender, redisTemplate, random);
  }

  @Test
  @DisplayName("메일 발송 성공")
  void sendVerificationCode() {
    String email = "test@email.com";

    when(memberService.existMemberByEmail(any())).thenReturn(false);
    ValueOperations<String, Object> operations = mock(ValueOperations.class);
    when(redisTemplate.opsForValue()).thenReturn(operations);
    doNothing().when(operations).set(anyString(), anyString(), anyLong(), any());

    mailService.sendVerificationCode(email);

    verify(javaMailSender, times(1)).send(any(SimpleMailMessage.class));
  }

  @Test
  @DisplayName("메일 발송 실패 - 중복 이메일")
  void sendVerificationCodeFailed() {
    when(memberService.existMemberByEmail(any())).thenReturn(true);

    assertThrows(MemberEmailAlreadyUsedException.class,
            () -> mailService.sendVerificationCode("email"));
  }

  @Test
  @DisplayName("인증번호 확인")
  void checkVerificationCode() {
    String verificationCode = "123123";
    MailVerificationRequestDto requestDto =
            new MailVerificationRequestDto("test@email.com", verificationCode);

    ValueOperations<String, Object> valueOperations = mock(ValueOperations.class);
    when(redisTemplate.opsForValue()).thenReturn(valueOperations);
    when(valueOperations.get(any())).thenReturn(verificationCode);
    when(redisTemplate.delete(any())).thenReturn(1L);

    assertTrue(mailService.checkVerificationCode(requestDto));
  }

  @Test
  @DisplayName("인증번호 확인 실패 - 인증번호 틀림")
  void checkVerificationCodeFailed() {
    String verificationCode = "123123";
    MailVerificationRequestDto requestDto =
            new MailVerificationRequestDto("test@email.com", "321321");

    ValueOperations<String, Object> valueOperations = mock(ValueOperations.class);
    when(redisTemplate.opsForValue()).thenReturn(valueOperations);
    when(valueOperations.get(any())).thenReturn(verificationCode);
    when(redisTemplate.delete(any())).thenReturn(1L);

    assertThrows(EmailNotVerificationException.class, () -> mailService.checkVerificationCode(requestDto));
  }
}