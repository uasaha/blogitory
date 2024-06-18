package com.blogitory.blog.member.controller;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.blogitory.blog.config.TestSecurityConfig;
import com.blogitory.blog.member.dto.MemberLoginRequestDto;
import com.blogitory.blog.member.dto.MemberUpdateNameRequestDto;
import com.blogitory.blog.member.dto.MemberUpdateProfileRequestDto;
import com.blogitory.blog.member.service.MemberService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

/**
 * Member Rest Controller test.
 *
 * @author woonseok
 * @since 1.0
 */
@WebMvcTest(value = {MemberRestController.class, TestSecurityConfig.class})
class MemberRestControllerTest {
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
   * Sets up.
   */
  @BeforeEach
  void setUp() {
  }

  /**
   * Update name.
   *
   * @throws Exception the exception
   */
  @Test
  @DisplayName("이름 수정")
  @WithMockUser("1")
  void updateName() throws Exception {
    MemberUpdateNameRequestDto requestDto = new MemberUpdateNameRequestDto();
    ReflectionTestUtils.setField(requestDto, "name", "newName");

    doNothing().when(memberService).updateName(1, requestDto);

    mvc.perform(put("/api/v1/users/profile/name")
            .content(objectMapper.writeValueAsString(requestDto))
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent())
            .andDo(print());
  }

  /**
   * Update open email.
   *
   * @throws Exception the exception
   */
  @Test
  @DisplayName("이메일 수정")
  @WithMockUser("1")
  void updateOpenEmail() throws Exception {
    MemberUpdateProfileRequestDto requestDto = new MemberUpdateProfileRequestDto();
    ReflectionTestUtils.setField(requestDto, "content", "content");

    doNothing().when(memberService).updateOpenEmail(1, requestDto);

    mvc.perform(put("/api/v1/users/profile/open-email")
                    .content(objectMapper.writeValueAsString(requestDto))
                    .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent())
            .andDo(print());
  }

  /**
   * Update github.
   *
   * @throws Exception the exception
   */
  @Test
  @DisplayName("깃허브 수정")
  @WithMockUser("1")
  void updateGithub() throws Exception {
    MemberUpdateProfileRequestDto requestDto = new MemberUpdateProfileRequestDto();
    ReflectionTestUtils.setField(requestDto, "content", "content");

    doNothing().when(memberService).updateGithub(1, requestDto);

    mvc.perform(put("/api/v1/users/profile/github")
                    .content(objectMapper.writeValueAsString(requestDto))
                    .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent())
            .andDo(print());
  }

  /**
   * Update facebook.
   *
   * @throws Exception the exception
   */
  @Test
  @DisplayName("페이스북 수정")
  @WithMockUser("1")
  void updateFacebook() throws Exception {
    MemberUpdateProfileRequestDto requestDto = new MemberUpdateProfileRequestDto();
    ReflectionTestUtils.setField(requestDto, "content", "content");

    doNothing().when(memberService).updateFacebook(1, requestDto);

    mvc.perform(put("/api/v1/users/profile/facebook")
                    .content(objectMapper.writeValueAsString(requestDto))
                    .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent())
            .andDo(print());
  }

  /**
   * Update x.
   *
   * @throws Exception the exception
   */
  @Test
  @DisplayName("X 수정")
  @WithMockUser("1")
  void updateX() throws Exception {
    MemberUpdateProfileRequestDto requestDto = new MemberUpdateProfileRequestDto();
    ReflectionTestUtils.setField(requestDto, "content", "content");

    doNothing().when(memberService).updateX(1, requestDto);

    mvc.perform(put("/api/v1/users/profile/x")
                    .content(objectMapper.writeValueAsString(requestDto))
                    .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent())
            .andDo(print());
  }

  /**
   * Update homepage.
   *
   * @throws Exception the exception
   */
  @Test
  @DisplayName("홈페이지 수정")
  @WithMockUser("1")
  void updateHomepage() throws Exception {
    MemberUpdateProfileRequestDto requestDto = new MemberUpdateProfileRequestDto();
    ReflectionTestUtils.setField(requestDto, "content", "content");

    doNothing().when(memberService).updateHomepage(1, requestDto);

    mvc.perform(put("/api/v1/users/profile/homepage")
                    .content(objectMapper.writeValueAsString(requestDto))
                    .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent())
            .andDo(print());
  }

  /**
   * In duplicated name.
   *
   * @throws Exception the exception
   */
  @Test
  @DisplayName("name 중복 검사")
  void inDuplicatedName() throws Exception {
    String name = "name";

    when(memberService.isDuplicateUsername(anyString())).thenReturn(false);

    mvc.perform(get("/api/v1/users/username/verification")
            .param("username", name)
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().string("false"))
            .andDo(print());
  }

  /**
   * In duplicated name true.
   *
   * @throws Exception the exception
   */
  @Test
  @DisplayName("name 중복 검사 true")
  void inDuplicatedNameTrue() throws Exception {
    String name = "name";

    when(memberService.isDuplicateUsername(anyString())).thenReturn(true);

    mvc.perform(get("/api/v1/users/username/verification")
                    .param("username", name)
                    .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().string("true"))
            .andDo(print());
  }
}