package com.blogitory.blog.follow.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.blogitory.blog.blog.service.BlogService;
import com.blogitory.blog.config.TestSecurityConfig;
import com.blogitory.blog.follow.service.FollowService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

/**
 * Follow rest controller test.
 *
 * @author woonseok
 * @Date 2024-08-26
 * @since 1.0
 **/
@WebMvcTest(value = { FollowRestController.class, TestSecurityConfig.class })
class FollowRestControllerTest {

  @Autowired
  MockMvc mvc;

  @MockBean
  FollowService followService;

  @MockBean
  BlogService blogService;

  @WithMockUser("1")
  @Test
  void follow() throws Exception {
    doNothing().when(followService).follow(anyInt(), anyString());

    mvc.perform(post("/api/follow/@user"))
            .andExpect(status().isCreated());
  }

  @WithMockUser("1")
  @Test
  void unfollow() throws Exception {
    doNothing().when(followService).unFollow(anyInt(), anyString());

    mvc.perform(delete("/api/follow/@user"))
            .andExpect(status().isNoContent());
  }
}