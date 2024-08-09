package com.blogitory.blog.posts.controller;

import com.blogitory.blog.blog.dto.response.GetBlogResponseDto;
import com.blogitory.blog.blog.dto.response.GetBlogWithCategoryResponseDto;
import com.blogitory.blog.blog.service.BlogService;
import com.blogitory.blog.commons.annotaion.RoleUser;
import com.blogitory.blog.posts.dto.request.SaveTempPostsDto;
import com.blogitory.blog.posts.dto.response.GetPostResponseDto;
import com.blogitory.blog.posts.service.PostsService;
import com.blogitory.blog.security.util.SecurityUtils;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Posts controller.
 *
 * @author woonseok
 * @Date 2024-08-01
 * @since 1.0
 **/
@Controller
@RequiredArgsConstructor
public class PostsController {
  private final BlogService blogService;
  private final PostsService postsService;

  /**
   * Posts issue page.
   *
   * @param tp    temp posts id
   * @param model model
   * @return page
   */
  @RoleUser
  @GetMapping("/posts")
  public String issuePostsPage(@RequestParam String tp, Model model) {
    Integer memberNo = SecurityUtils.getCurrentUserNo();

    SaveTempPostsDto saveDto = postsService.loadTempPosts(tp, memberNo);
    List<GetBlogWithCategoryResponseDto> blogs = blogService.getBlogListWithCategory(memberNo);

    model.addAttribute("blogs", blogs);
    model.addAttribute("posts", saveDto);

    return "index/posts/write";
  }

  /**
   * Posts page.
   *
   * @param username username
   * @param blogUrl  blog url
   * @param postUrl  post url
   * @param model    model
   * @return page
   */
  @GetMapping("/@{username}/{blogUrl}/{postUrl}")
  public String postsPage(@PathVariable String username,
                          @PathVariable String blogUrl,
                          @PathVariable String postUrl,
                          Model model) {
    String blogKey = "@" + username + "/" + blogUrl;
    String postKey = blogKey + "/" + postUrl;

    GetBlogResponseDto blogResponse = blogService.getBlogByUrl(blogKey);
    GetPostResponseDto postsResponse = postsService.getPostByUrl(postKey);

    model.addAttribute("blog", blogResponse);
    model.addAttribute("posts", postsResponse);

    return "blog/main/posts";
  }
}
