package com.blogitory.blog.mail.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.blogitory.blog.blog.service.BlogService;
import com.blogitory.blog.config.TestSecurityConfig;
import com.blogitory.blog.mail.dto.request.GetMailVerificationRequestDto;
import com.blogitory.blog.mail.service.MailService;
import com.blogitory.blog.member.exception.MemberEmailAlreadyUsedException;
import com.blogitory.blog.member.service.MemberService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

/**
 * Mail Rest Controller test.
 *
 * @author woonseok
 * @since 1.0
 */
@WebMvcTest(value = {MailRestController.class, TestSecurityConfig.class})
class MailRestControllerTest {

  @Autowired
  MockMvc mvc;

  @Autowired
  ObjectMapper objectMapper;

  @MockBean
  MemberService memberService;

  @MockBean
  MailService mailService;

  @MockBean
  BlogService blogService;

  @Test
  @DisplayName("인증번호 발송")
  void issueVerificationCode() throws Exception {
    String email = "test@email.com";

    doNothing().when(mailService).sendVerificationCode(email);

    mvc.perform(MockMvcRequestBuilders.get("/api/mail/verification")
            .param("email", email))
            .andExpect(status().isOk())
            .andDo(print());
  }

  @Test
  @DisplayName("인증번호 발송 실패 - 중복된 이메일")
  void issueVerificationCodeFailed() throws Exception {
    String email = "test@email.com";

    doThrow(MemberEmailAlreadyUsedException.class)
            .when(mailService).sendVerificationCode(anyString());

    mvc.perform(MockMvcRequestBuilders.get("/api/mail/verification")
            .param("email", email)
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isConflict());
  }

  @Test
  @DisplayName("인증번호 확인")
  void checkVerificationCode() throws Exception {
    String email = "test@email.com";
    String verificationCode = "123456";
    GetMailVerificationRequestDto getMailVerificationRequestDto =
            new GetMailVerificationRequestDto(email, verificationCode);

    when(mailService.checkVerificationCode(any()))
            .thenReturn(true);

    mvc.perform(MockMvcRequestBuilders.post("/api/mail/verification")
            .content(objectMapper.writeValueAsString(getMailVerificationRequestDto))
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("verified").value(true))
            .andDo(print());
  }
}