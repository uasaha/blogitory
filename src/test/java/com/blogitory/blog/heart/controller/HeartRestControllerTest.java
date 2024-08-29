package com.blogitory.blog.heart.controller;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.blogitory.blog.blog.service.BlogService;
import com.blogitory.blog.config.TestSecurityConfig;
import com.blogitory.blog.heart.service.HeartService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

/**
 * Heart rest controller test.
 *
 * @author woonseok
 * @Date 2024-08-29
 * @since 1.0
 **/
@WebMvcTest(value = {HeartRestController.class, TestSecurityConfig.class})
class HeartRestControllerTest {
  @Autowired
  MockMvc mvc;

  @Autowired
  ObjectMapper objectMapper;

  @MockBean
  BlogService blogService;

  @MockBean
  HeartService heartService;

  @WithMockUser("1")
  @Test
  void checkHeart() throws Exception {
    when(heartService.existHeart(anyInt(), anyString())).thenReturn(false);

    mvc.perform(get("/api/@username/blogUrl/postsUrl/heart"))
            .andExpect(status().isOk());
  }

  @Test
  void checkHeartNotAuthenticated() throws Exception {
    when(heartService.existHeart(anyInt(), anyString())).thenReturn(false);

    mvc.perform(get("/api/@username/blogUrl/postsUrl/heart"))
            .andExpect(status().isUnauthorized());
  }

  @Test
  void countHearts() throws Exception {
    when(heartService.countHeart(anyString())).thenReturn(3L);

    mvc.perform(get("/api/@username/blogUrl/postsUrl/hearts/count"))
            .andExpect(status().isOk());
  }

  @WithMockUser("1")
  @Test
  void doHeart() throws Exception {
    doNothing().when(heartService).heart(anyInt(), anyString());

    mvc.perform(post("/api/@username/blogUrl/postsUrl/heart"))
            .andExpect(status().isCreated());
  }

  @WithMockUser("1")
  @Test
  void deleteHeart() throws Exception {
    doNothing().when(heartService).deleteHeart(anyInt(), anyString());

    mvc.perform(delete("/api/@username/blogUrl/postsUrl/heart"))
            .andExpect(status().isNoContent());
  }
}