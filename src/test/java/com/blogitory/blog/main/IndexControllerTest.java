package com.blogitory.blog.main;


import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.blogitory.blog.commons.config.WebMvcConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

/**
 * Index Controller Test.
 *
 * @author woonseok
 * @since 1.0
 **/
@WebMvcTest(value = IndexController.class, excludeAutoConfiguration = WebMvcConfig.class)
@WithMockUser
class IndexControllerTest {

  @Autowired
  MockMvc mvc;

  @BeforeEach
  void setUp() {

  }

  @Test
  void indexPage() throws Exception {
    mvc.perform(MockMvcRequestBuilders.get("/"))
            .andExpect(status().isOk())
            .andExpect(content().string(containsString("Blogitory")));
  }

  @Test
  void signupPage() throws Exception{
    mvc.perform(MockMvcRequestBuilders.get("/signup"))
            .andExpect(status().isOk())
            .andExpect(content().string(containsString("Blogitory")));
  }
}