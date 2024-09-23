package com.blogitory.blog.main;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.blogitory.blog.blog.service.BlogService;
import com.blogitory.blog.config.TestSecurityConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

/**
 * Index rest controller test.
 *
 * @author woonseok
 * @since 1.0
 **/
@WebMvcTest(value = {IndexRestController.class, TestSecurityConfig.class})
class IndexRestControllerTest {

  @Autowired
  MockMvc mvc;

  @MockBean
  BlogService blogService;

  @Test
  void getRobots() throws Exception {
    mvc.perform(get("/robots.txt")
            .contentType(MediaType.TEXT_PLAIN_VALUE))
            .andExpect(status().isOk());
  }
}