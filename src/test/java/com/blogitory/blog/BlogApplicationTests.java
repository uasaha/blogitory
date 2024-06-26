package com.blogitory.blog;

import static org.junit.jupiter.api.Assertions.assertNotEquals;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ActiveProfiles;

/**
 * The type Blog application tests.
 */
@SpringBootTest(classes = ApplicationContext.class)
@ActiveProfiles("test")
@Slf4j
class BlogApplicationTests {
  @Autowired
  ApplicationContext context;

  /**
   * Context loads.
   */
  @Test
	void contextLoads() {
    assertNotEquals("Spring", context.getApplicationName());
  }
}
