package com.blogitory.blog.posts.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import com.blogitory.blog.blog.service.BlogService;
import com.blogitory.blog.config.TestSecurityConfig;
import com.blogitory.blog.posts.dto.request.SaveTempPostsDto;
import com.blogitory.blog.posts.dto.response.CreatePostsResponseDto;
import com.blogitory.blog.posts.service.PostsService;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

/**
 * Posts Rest controller test.
 *
 * @author woonseok
 * @Date 2024-08-08
 * @since 1.0
 **/
@WebMvcTest(value = {PostsRestController.class, TestSecurityConfig.class})
class PostsRestControllerTest {

  @Autowired
  MockMvc mvc;

  @Autowired
  ObjectMapper objectMapper;

  @MockBean
  PostsService postsService;

  @MockBean
  BlogService blogService;

  @BeforeEach
  void setUp() {
  }

  @WithMockUser("1")
  @Test
  @DisplayName("임시 게시물 키 발급")
  void key() throws Exception {
    String key = "key";

    when(postsService.getTempPostsId(any())).thenReturn(key);

    mvc.perform(get("/api/posts/key"))
            .andExpect(status().isOk())
            .andExpect(content().string(key));
  }

  @WithMockUser("1")
  @Test
  @DisplayName("게시물 임시 저장")
  void tempSave() throws Exception {
    SaveTempPostsDto postsDto = new SaveTempPostsDto(
            1L,
            1,
            "title",
            1L,
            "url",
            "summary",
            "thumb",
            "details",
            List.of());

    doNothing().when(postsService).saveTempPosts(anyString(), any(), any());

    mvc.perform(post("/api/posts/tp-qwejnqwe")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(postsDto)))
            .andExpect(status().isOk());
  }

  @WithMockUser("1")
  @Test
  @DisplayName("게시물 등록")
  void createPosts() throws Exception {
    SaveTempPostsDto postsDto = new SaveTempPostsDto(
            1L,
            1,
            "title",
            1L,
            "url",
            "summary",
            "thumb",
            "details",
            List.of());

    CreatePostsResponseDto responseDto = new CreatePostsResponseDto("postsUrl");

    when(postsService.createPosts(anyString(), any(), any())).thenReturn(responseDto);

    mvc.perform(post("/api/posts?tp=lqgnqwngl")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(postsDto)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.postsUrl").value("postsUrl"));
  }

  @WithMockUser("1")
  @Test
  @DisplayName("임시 게시물 삭제")
  void deleteTemp() throws Exception {
    doNothing().when(postsService).deleteTempPosts(anyInt(), anyString());

    mvc.perform(delete("/api/posts?tp=qklwntqnwtq"))
            .andExpect(status().isOk());
  }
}