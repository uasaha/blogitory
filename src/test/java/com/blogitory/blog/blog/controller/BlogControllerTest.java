package com.blogitory.blog.blog.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.blogitory.blog.blog.dto.response.GetBlogResponseDto;
import com.blogitory.blog.blog.service.BlogService;
import com.blogitory.blog.category.dto.GetCategoryResponseDto;
import com.blogitory.blog.config.TestSecurityConfig;
import com.blogitory.blog.tag.dto.GetTagResponseDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

/**
 * BlogControllerTest.
 *
 * @author woonseok
 * @Date 2024-08-08
 * @since 1.0
 **/
@WebMvcTest(value = {BlogController.class, TestSecurityConfig.class})
class BlogControllerTest {

  @Autowired
  MockMvc mockMvc;

  @Autowired
  ObjectMapper objectMapper;

  @MockBean
  BlogService blogService;

  @BeforeEach
  void setUp() {
  }

  @Test
  void blog() throws Exception {
    List<GetCategoryResponseDto> categories =
            List.of(new GetCategoryResponseDto(1L, "category"));
    List<GetTagResponseDto> tags = List.of(new GetTagResponseDto("tag"));

    GetBlogResponseDto responseDto = new GetBlogResponseDto(
            "thumbUrl",
            "thumbOriginName",
            "@test/test",
            "test",
            "test",
            "test",
            "test",
            categories,
            tags);

    when(blogService.getBlogByUrl(any())).thenReturn(responseDto);

    mockMvc.perform(get("/@test/test"))
            .andExpect(status().isOk())
            .andDo(print());
  }
}