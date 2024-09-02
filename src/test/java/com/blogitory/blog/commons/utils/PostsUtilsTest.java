package com.blogitory.blog.commons.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

/**
 * Posts utils test.
 *
 * @author woonseok
 * @Date 2024-09-02
 * @since 1.0
 **/
class PostsUtilsTest {

  @Test
  void extractSummary() {
    String details = "### Hello, world!";
    String expect = "Hello, world";

    String actual = PostsUtils.extractSummary(details);

    assertEquals(expect, actual);
  }

  @Test
  void extractSummaryOver300() {
    String details = "### Hello, world!\n### Hello, world!\n### Hello, world!\n### Hello, world!\n### Hello, world!\n### Hello, world!\n### Hello, world! \n### Hello, world!\n### Hello, world!\n### Hello, world!\n### Hello, world!\n### Hello, world!\n### Hello, world!\n### Hello, world!\n### Hello, world!\n### Hello, world!\n### Hello, world!\n### Hello, world!\n### Hello, world!\n### Hello, world!\n### Hello, world!\n### Hello, world!";
    String expect = "Hello, world!\nHello, world!\nHello, world!\nHello, world!\nHello, world!\nHello, world!\nHello, world!\nHello, world!\nHello, world!\nHello, world!\nHello, world!\nHello, world!\nHello, world!\nHello, world!\nHello, world!\nHello, world!\nHello, world!\nHello, world!\nHello, world!\nHello, world!\nHello, world!\nHello,";

    String actual = PostsUtils.extractSummary(details);

    assertEquals(expect, actual);
  }
}