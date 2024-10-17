package com.blogitory.blog.viewer.controller;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.blogitory.blog.commons.config.WebMvcConfig;
import com.blogitory.blog.config.TestSecurityConfig;
import com.blogitory.blog.viewer.service.ViewerService;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

/**
 * Viewer rest controller test.
 *
 * @author woonseok
 * @since 1.0
 **/
@WebMvcTest(value = {ViewerRestController.class, TestSecurityConfig.class},
        excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = {WebMvcConfig.class}))
class ViewerRestControllerTest {

  @Autowired
  MockMvc mvc;

  @MockBean
  ViewerService viewerService;

  @WithMockUser("1")
  @Test
  void getMonthlyViewerCounts() throws Exception {
    when(viewerService.getViewerMonthlyCount(anyInt(), anyString()))
            .thenReturn(List.of());

    mvc.perform(get("/api/@test/test/test/viewers/statistic"))
            .andExpect(status().isOk());
  }
}