package com.blogitory.blog.member.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.blogitory.blog.member.entity.Member;
import com.blogitory.blog.member.entity.MemberDummy;
import com.blogitory.blog.member.service.MemberService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

/**
 * 설명 작성 필!
 *
 * @author woonseok
 * @since 1.0
 **/
@WebMvcTest(MemberController.class)
@WithMockUser
class MemberControllerTest {
  @Autowired
  MockMvc mvc;

  @Autowired
  ObjectMapper objectMapper;

  @MockBean
  MemberService memberService;

  @Test
  void signup() throws Exception {
    Member member = MemberDummy.dummy();

    doNothing().when(memberService).signup(any());

    mvc.perform(MockMvcRequestBuilders.post("/signup").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .param("name", member.getName())
            .param("email", member.getEmail())
            .param("pwd", member.getPassword()))
            .andExpect(status().is3xxRedirection())
            .andDo(print());
  }
}