package com.blogitory.blog.category.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.blogitory.blog.category.dto.CategoryCreateResponseDto;
import com.blogitory.blog.category.service.CategoryService;
import com.blogitory.blog.config.TestSecurityConfig;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

/**
 * Category rest controller test.
 *
 * @author woonseok
 * @Date 2024-07-29
 * @since 1.0
 **/
@WebMvcTest(value = {CategoryRestController.class, TestSecurityConfig.class})
class CategoryRestControllerTest {
  @Autowired
  MockMvc mvc;

  @Autowired
  ObjectMapper objectMapper;

  @MockBean
  CategoryService categoryService;

  @WithMockUser("1")
  @Test
  @DisplayName("카테고리 생성")
  void createCategory() throws Exception {
    CategoryCreateResponseDto responseDto =
            new CategoryCreateResponseDto(1L, "category");

    when(categoryService.createCategory(any(), anyString(), any())).thenReturn(responseDto);

    mvc.perform(post("/api/categories")
            .param("name", "category")
            .param("blogUrl", "blog")
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.categoryNo").value(1))
            .andExpect(jsonPath("$.name").value("category"));
  }

  @WithMockUser("1")
  @Test
  @DisplayName("카테고리 수정")
  void updateCategory() throws Exception {
    doNothing().when(categoryService).updateCategory(any(), anyString(), any());

    mvc.perform(put("/api/categories/1")
            .param("name", "category"))
            .andExpect(status().isNoContent());
  }

  @WithMockUser("1")
  @Test
  @DisplayName("카테고리 삭제")
  void deleteCategory() throws Exception {
    doNothing().when(categoryService).deleteCategory(anyLong(), anyInt());

    mvc.perform(delete("/api/categories/1"))
            .andExpect(status().isNoContent());
  }
}