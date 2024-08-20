package com.blogitory.blog.comment.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.blogitory.blog.blog.service.BlogService;
import com.blogitory.blog.comment.dto.request.CreateCommentRequestDto;
import com.blogitory.blog.comment.dto.request.ModifyCommentRequestDto;
import com.blogitory.blog.comment.dto.response.GetChildCommentResponseDto;
import com.blogitory.blog.comment.dto.response.GetCommentResponseDto;
import com.blogitory.blog.comment.service.CommentService;
import com.blogitory.blog.commons.dto.Pages;
import com.blogitory.blog.config.TestSecurityConfig;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;

/**
 * CommentRestController test.
 *
 * @author woonseok
 * @Date 2024-08-08
 * @since 1.0
 **/
@WebMvcTest(value = {CommentRestController.class, TestSecurityConfig.class})
class CommentRestControllerTest {

  @Autowired
  MockMvc mockMvc;

  @Autowired
  ObjectMapper objectMapper;

  @MockBean
  BlogService blogService;

  @MockBean
  CommentService commentService;

  @WithMockUser("1")
  @Test
  @DisplayName("댓글 작성 api")
  void createComment() throws Exception {
    doNothing().when(commentService).createComment(anyInt(), any(), any());
    CreateCommentRequestDto requestDto = new CreateCommentRequestDto();
    ReflectionTestUtils.setField(requestDto, "postsUrl", "posts");
    ReflectionTestUtils.setField(requestDto, "content", "my comment");

    mockMvc.perform(post("/api/comments")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(requestDto)))
            .andExpect(status().isCreated());
  }

  @Test
  @DisplayName("댓글 조회")
  void getComments() throws Exception {
    GetCommentResponseDto responseDto = new GetCommentResponseDto(
            "name", "username", "userpfp",
            1L, "content", false,
            LocalDateTime.now(), null, 0L);

    Pages<GetCommentResponseDto> pages = new Pages<>(List.of(responseDto),
            0L, false, false, 1L);

    when(commentService.getComments(anyString(), any()))
            .thenReturn(pages);

    mockMvc.perform(get("/api/@username/blogurl/postsurl/comments?page=0")
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.body[0].name").value("name"));
  }

  @Test
  @DisplayName("답글 조회")
  void getChildComments() throws Exception {
    GetChildCommentResponseDto responseDto = new GetChildCommentResponseDto(
            "name", "username", "pfp", 1L, 2L,
            "contents", false, LocalDateTime.now(), null);

    Pages<GetChildCommentResponseDto> pages = new Pages<>(
            List.of(responseDto), 0L, false, false, 1L);

    when(commentService.getChildComments(anyString(), any(), any()))
            .thenReturn(pages);

    mockMvc.perform(get("/api/@username/blogurl/postsurl/comments/1/child?page=0")
                    .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.body[0].name").value("name"));
  }

  @Test
  @WithMockUser("1")
  @DisplayName("댓글 수정")
  void modifyComment() throws Exception {
    ModifyCommentRequestDto requestDto = new ModifyCommentRequestDto();
    ReflectionTestUtils.setField(requestDto, "contents", "contents");

    doNothing().when(commentService).modifyComment(anyInt(), any(), any());

    mockMvc.perform(put("/api/comments/1")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(requestDto)))
            .andExpect(status().isOk());
  }

  @Test
  @WithMockUser("1")
  @DisplayName("댓글 삭제")
  void deleteComment() throws Exception {
    doNothing().when(commentService).deleteComment(anyInt(), any());

    mockMvc.perform(delete("/api/comments/1")
                    .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk());
  }
}