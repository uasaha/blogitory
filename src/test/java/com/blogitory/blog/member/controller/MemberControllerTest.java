package com.blogitory.blog.member.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.blogitory.blog.blog.dto.BlogProfileResponseDto;
import com.blogitory.blog.config.TestSecurityConfig;
import com.blogitory.blog.member.dto.MemberProfileLinkResponseDto;
import com.blogitory.blog.member.dto.MemberProfileResponseDto;
import com.blogitory.blog.member.entity.Member;
import com.blogitory.blog.member.entity.MemberDummy;
import com.blogitory.blog.member.service.MemberService;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

/**
 * Member Controller Test.
 */
@WebMvcTest(value = {MemberController.class, TestSecurityConfig.class})
class MemberControllerTest {
  /**
   * The Mvc.
   */
  @Autowired
  MockMvc mvc;

  /**
   * The Object mapper.
   */
  @Autowired
  ObjectMapper objectMapper;

  /**
   * The Member service.
   */
  @MockBean
  MemberService memberService;

  /**
   * Signup.
   *
   * @throws Exception the exception
   */
  @Test
  @DisplayName("회원가입 페이지")
  void signup() throws Exception {
    Member member = MemberDummy.dummy();

    doNothing().when(memberService).signup(any());

    mvc.perform(MockMvcRequestBuilders.post("/signup").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .param("username", member.getUsername())
            .param("name", member.getName())
            .param("email", member.getEmail())
            .param("pwd", member.getPassword()))
            .andExpect(status().is3xxRedirection())
            .andDo(print());
  }

  @Test
  @DisplayName("프로필 페이지")
  void profile() throws Exception {
    MemberProfileResponseDto profileDto =
            new MemberProfileResponseDto(
                    "username",
                    "name",
                    "bio",
                    "profileThumb",
                    "introEmail",
                    List.of(new MemberProfileLinkResponseDto(1L, "link")),
                    List.of(new BlogProfileResponseDto("url", "blog", "bio")),
                    1L,
                    1L);

    when(memberService.getProfileByUsername(anyString())).thenReturn(profileDto);

    mvc.perform(MockMvcRequestBuilders.get("/@" + profileDto.getUsername()).with(csrf()))
            .andExpect(status().isOk());
  }
}