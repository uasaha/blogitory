package com.blogitory.blog.visitant.controller;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.blogitory.blog.blog.service.BlogService;
import com.blogitory.blog.config.TestSecurityConfig;
import com.blogitory.blog.visitant.service.VisitantService;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

/**
 * VisitantRestControllerTest.
 *
 * @author woonseok
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

  @WithMockUser(value = "1", authorities = "ROLE_ADMIN")
  @Test
  void saveAndDeleteNow() throws Exception {
    doNothing().when(visitantService).saveAndDelete();

    mvc.perform(get("/api/admin/visitants/now"))
            .andExpect(status().isOk());
  }

  @WithMockUser("1")
  @Test
  void getMonthlyVisitantsCounts() throws Exception {
    when(visitantService.getVisitantMonthlyCount(anyInt(), anyString()))
            .thenReturn(List.of());

    mvc.perform(get("/api/@test/test/visitants/statistics"))
            .andExpect(status().isOk());
  }
}