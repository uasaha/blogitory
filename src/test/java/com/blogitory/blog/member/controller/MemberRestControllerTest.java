package com.blogitory.blog.member.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.blogitory.blog.config.TestSecurityConfig;
import com.blogitory.blog.member.dto.MemberUpdateProfileRequestDto;
import com.blogitory.blog.member.service.MemberService;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;

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

  @Test
  @DisplayName("프로필 업데이트")
  void updateProfile() throws Exception {
    SecurityContext securityContext = SecurityContextHolder.getContext();
    Authentication authentication = new UsernamePasswordAuthenticationToken(1, "email", List.of(new SimpleGrantedAuthority("ROLE_USER")));
    securityContext.setAuthentication(authentication);

    MemberUpdateProfileRequestDto requestDto = new MemberUpdateProfileRequestDto();
    ReflectionTestUtils.setField(requestDto, "name", "name");
    ReflectionTestUtils.setField(requestDto, "bio", "bio");
    ReflectionTestUtils.setField(requestDto, "email", "email@email.com");
    ReflectionTestUtils.setField(requestDto, "linkList", List.of("link"));

    doNothing().when(memberService).updateProfile(any(), any());

    mvc.perform(put("/api/v1/users/profiles")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(requestDto)))
            .andExpect(status().isNoContent())
            .andDo(print());
  }
}