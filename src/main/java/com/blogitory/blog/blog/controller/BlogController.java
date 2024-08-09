package com.blogitory.blog.blog.controller;

import com.blogitory.blog.blog.dto.response.GetBlogResponseDto;
import com.blogitory.blog.blog.service.BlogService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * Controller for Profiles and Blogs.
 *
 * @author woonseok
 * @since 1.0
 **/
@Controller
@Slf4j
@RequiredArgsConstructor
public class BlogController {
  private final BlogService blogService;

  /**
   * Go to blog page.
   *
   * @param username username
   * @param blogUrl  blog url
   * @param model    model
   * @return page
   */
  @GetMapping("/@{username}/{blogUrl}")
  public String blog(@PathVariable("username") String username,
                     @PathVariable("blogUrl") String blogUrl,
                     Model model) {
    String url = "@" + username + "/" + blogUrl;
    GetBlogResponseDto response = blogService.getBlogByUrl(url);
    model.addAttribute("blog", response);

    return "blog/main/index";
  }
}
