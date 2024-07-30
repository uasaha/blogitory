package com.blogitory.blog.blog.controller;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.blogitory.blog.blog.dto.request.BlogCreateRequestDto;
import com.blogitory.blog.blog.dto.request.BlogModifyRequestDto;
import com.blogitory.blog.blog.service.BlogService;
import com.blogitory.blog.config.TestSecurityConfig;
import com.blogitory.blog.image.dto.ThumbnailUpdateResponseDto;
import com.blogitory.blog.image.service.ImageService;
import com.blogitory.blog.member.dto.request.MemberLeftRequestDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;

/**
 * Blog rest controller test.
 *
 * @author woonseok
 * @Date 2024-07-29
 * @since 1.0
 **/
@WebMvcTest(value = {BlogRestController.class, TestSecurityConfig.class})
class BlogRestControllerTest {
  @Autowired
  MockMvc mvc;

  @Autowired
  ObjectMapper objectMapper;

  @MockBean
  BlogService blogService;

  @MockBean
  ImageService imageService;

  @Test
  @WithMockUser("1")
  void create() throws Exception {
    BlogCreateRequestDto requestDto = new BlogCreateRequestDto();
    ReflectionTestUtils.setField(requestDto, "name", "blog");
    ReflectionTestUtils.setField(requestDto, "url", "blog");
    ReflectionTestUtils.setField(requestDto, "bio", "blog bio");

    doNothing().when(blogService).createBlog(requestDto, 1);

    mvc.perform(post("/api/blogs")
            .content(objectMapper.writeValueAsString(requestDto))
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isCreated());
  }

  @Test
  @WithMockUser("1")
  void update() throws Exception {
    BlogModifyRequestDto requestDto = new BlogModifyRequestDto();
    ReflectionTestUtils.setField(requestDto, "name", "blog");
    ReflectionTestUtils.setField(requestDto, "bio", "blog");
    String blogUrl = "@blog/blog";

    doNothing().when(blogService).updateBlog(blogUrl, requestDto, 1);

    mvc.perform(put("/api/blogs")
            .param("url", blogUrl)
            .content(objectMapper.writeValueAsString(requestDto))
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());
  }

  @Test
  @WithMockUser("1")
  void updateBlogThumb() throws Exception {
    MockMultipartFile file = new MockMultipartFile("file",
            "content".getBytes(StandardCharsets.UTF_8));
    String blogUrl = "@blog/blog";

    ThumbnailUpdateResponseDto responseDto =
            new ThumbnailUpdateResponseDto("url", file.getName());

    when(imageService.updateBlogThumbnail(1, blogUrl, file))
            .thenReturn(responseDto);

    mvc.perform(multipart("/api/blogs/thumbs")
            .file(file)
            .param("url", blogUrl)
            .contentType(MediaType.MULTIPART_FORM_DATA))
            .andExpect(status().isOk());
  }

  @Test
  @WithMockUser("1")
  void deleteBlogThumb() throws Exception {
    String blogUrl = "@blog/blog";

    doNothing().when(imageService).removeBlogThumbnail(1, blogUrl);

    mvc.perform(delete("/api/blogs/thumbs")
            .param("url", blogUrl))
            .andExpect(status().isNoContent());
  }

  @Test
  @WithMockUser("1")
  void deleteBlog() throws Exception {
    String blogUrl = "@blog/blog";
    MemberLeftRequestDto requestDto = new MemberLeftRequestDto();
    ReflectionTestUtils.setField(requestDto, "password", "password");

    doNothing().when(blogService).quitBlog(1, blogUrl, requestDto.getPassword());

    mvc.perform(post("/api/blogs/quit")
            .param("url", blogUrl)
            .content(objectMapper.writeValueAsString(requestDto))
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());
  }
}