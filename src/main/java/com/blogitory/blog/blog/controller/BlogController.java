package com.blogitory.blog.blog.controller;

import static com.blogitory.blog.commons.utils.UrlUtil.getBlogKey;

import com.blogitory.blog.blog.dto.response.GetBlogResponseDto;
import com.blogitory.blog.blog.service.BlogService;
import com.blogitory.blog.posts.dto.response.GetPopularPostResponseDto;
import com.blogitory.blog.posts.service.PostsService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
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
  private final PostsService postsService;

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
    String blogKey = getBlogKey(username, blogUrl);
    GetBlogResponseDto blogResponse = blogService.getBlogByUrl(blogKey);
    List<GetPopularPostResponseDto> postResponses = postsService.getPopularPostsByBlog(blogKey);

    model.addAttribute("blog", blogResponse);
    model.addAttribute("popularPosts", postResponses);

    return "blog/main/index";
  }

  /**
   * All posts page.
   *
   * @param username username
   * @param blogUrl  blog url
   * @param model    model
   * @param pageable pageable
   * @return all posts page
   */
  @GetMapping("/@{username}/{blogUrl}/posts")
  public String blogPosts(@PathVariable("username") String username,
                          @PathVariable("blogUrl") String blogUrl,
                          @PageableDefault Pageable pageable,
                          Model model) {
    String blogKey = getBlogKey(username, blogUrl);

    GetBlogResponseDto blogResponse = blogService.getBlogByUrl(blogKey);

    model.addAttribute("blog", blogResponse);
    model.addAttribute("pageable", pageable);

    return "blog/main/all-posts";
  }
}
