package com.blogitory.blog.visitant.controller;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.blogitory.blog.blog.service.BlogService;
import com.blogitory.blog.config.TestSecurityConfig;
import com.blogitory.blog.visitant.service.VisitantService;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

/**
 * VisitantRestControllerTest.
 *
 * @author woonseok
 * @Date 2024-09-11
 * @since 1.0
 **/
@WebMvcTest(value = {VisitantRestController.class, TestSecurityConfig.class})
class VisitantRestControllerTest {

  @Autowired
  MockMvc mvc;

  @Autowired
  ObjectMapper objectMapper;

  @MockBean
  VisitantService visitantService;

  @MockBean
  BlogService blogService;

  @Test
  void getVisitants() throws Exception {
    when(visitantService.getVisitantCount(anyString())).thenReturn(Map.of());

    mvc.perform(get("/api/@test/test/visitants"))
            .andExpect(status().isOk());
  }
}