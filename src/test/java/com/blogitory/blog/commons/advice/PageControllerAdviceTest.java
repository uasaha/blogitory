package com.blogitory.blog.commons.advice;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.blogitory.blog.blog.service.BlogService;
import com.blogitory.blog.category.exception.DuplicateCategoryException;
import com.blogitory.blog.commons.exception.NotFoundException;
import com.blogitory.blog.config.TestSecurityConfig;
import com.blogitory.blog.follow.service.FollowService;
import com.blogitory.blog.member.controller.MemberController;
import com.blogitory.blog.member.service.MemberService;
import com.blogitory.blog.security.exception.AuthenticationException;
import com.blogitory.blog.security.exception.AuthorizationException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

/**
 * PageControllerAdviceTest.
 *
 * @author woonseok
 * @Date 2024-07-30
 * @since 1.0
 **/
@WebMvcTest(value = {MemberController.class, TestSecurityConfig.class})
class PageControllerAdviceTest {

  @Autowired
  MockMvc mvc;

  @MockBean
  MemberService memberService;

  @MockBean
  FollowService followService;

  @MockBean
  BlogService blogService;

  @Test
  void handleAuthentication() throws Exception {
    when(memberService.getProfileByUsername(anyString())).thenThrow(AuthenticationException.class);

    mvc.perform(MockMvcRequestBuilders.get("/@test"))
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("/"));
  }

  @Test
  void handleBadRequest() throws Exception {
    when(memberService.getProfileByUsername(anyString())).thenThrow(DuplicateCategoryException.class);

    mvc.perform(MockMvcRequestBuilders.get("/@test"))
            .andExpect(status().isBadRequest());
  }

  @Test
  void handleNotFound() throws Exception {
    when(memberService.getProfileByUsername(anyString())).thenThrow(NotFoundException.class);

    mvc.perform(MockMvcRequestBuilders.get("/@test"))
            .andExpect(status().isNotFound());
  }

  @Test
  void handleForbidden() throws Exception {
    when(memberService.getProfileByUsername(anyString())).thenThrow(AuthorizationException.class);

    mvc.perform(MockMvcRequestBuilders.get("/@test"))
            .andExpect(status().isForbidden());
  }

  @Test
  void handleServerError() throws Exception {
    when(memberService.getProfileByUsername(anyString())).thenThrow(RuntimeException.class);

    mvc.perform(MockMvcRequestBuilders.get("/@test"))
            .andExpect(status().isInternalServerError());
  }
}