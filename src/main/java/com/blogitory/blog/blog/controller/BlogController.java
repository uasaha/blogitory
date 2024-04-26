package com.blogitory.blog.blog.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * 설명 작성 필!.
 *
 * @author woonseok
 * @since 1.0
 **/
@Controller
public class BlogController {

  @GetMapping("@{blogUrl}")
  public String blog(@PathVariable String blogUrl) {

    return "/blog/index";
  }
}
