package com.blogitory.blog.blog.controller;

import static com.blogitory.blog.commons.utils.UrlUtil.getBlogKey;

import com.blogitory.blog.blog.dto.response.GetBlogResponseDto;
import com.blogitory.blog.blog.service.BlogService;
import com.blogitory.blog.posts.dto.response.GetPopularPostResponseDto;
import com.blogitory.blog.posts.service.PostsService;
import com.blogitory.blog.visitant.aspect.point.Visitant;
import jakarta.servlet.http.HttpServletRequest;
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

  private static final String BLOG_ATTR = "blog";
  public static final String PAGEABLE_ATTR = "pageable";

  /**
   * Go to blog page.
   *
   * @param request HttpServletRequest
   * @param username username
   * @param blogUrl  blog url
   * @param model    model
   * @return page
   */
  @Visitant
  @GetMapping("/@{username}/{blogUrl}")
  public String blog(HttpServletRequest request,
                     @PathVariable("username") String username,
                     @PathVariable("blogUrl") String blogUrl,
                     Model model) {
    String blogKey = getBlogKey(username, blogUrl);
    GetBlogResponseDto blogResponse = blogService.getBlogByUrl(blogKey);
    List<GetPopularPostResponseDto> postResponses = postsService.getPopularPostsByBlog(blogKey);

    model.addAttribute(BLOG_ATTR, blogResponse);
    model.addAttribute("popularPosts", postResponses);

    return "blog/main/index";
  }

  /**
   * All posts page.
   *
   * @param request HttpServletRequest
   * @param username username
   * @param blogUrl  blog url
   * @param model    model
   * @param pageable pageable
   * @return all posts page
   */
  @Visitant
  @GetMapping("/@{username}/{blogUrl}/posts")
  public String blogPosts(HttpServletRequest request,
                          @PathVariable("username") String username,
                          @PathVariable("blogUrl") String blogUrl,
                          @PageableDefault Pageable pageable,
                          Model model) {
    String blogKey = getBlogKey(username, blogUrl);

    GetBlogResponseDto blogResponse = blogService.getBlogByUrl(blogKey);

    model.addAttribute(BLOG_ATTR, blogResponse);
    model.addAttribute(PAGEABLE_ATTR, pageable);

    return "blog/main/all-posts";
  }

  /**
   * Category post page.
   *
   * @param request HttpServletRequest
   * @param username     username
   * @param blogUrl      blog url
   * @param categoryName category name
   * @param pageable     pageable
   * @param model        model
   * @return category page
   */
  @Visitant
  @GetMapping("/@{username}/{blogUrl}/categories/{categoryName}")
  public String categoryPosts(HttpServletRequest request,
                              @PathVariable("username") String username,
                              @PathVariable("blogUrl") String blogUrl,
                              @PathVariable("categoryName") String categoryName,
                              @PageableDefault Pageable pageable,
                              Model model) {
    String blogKey = getBlogKey(username, blogUrl);

    GetBlogResponseDto blogResponse = blogService.getBlogByUrl(blogKey);

    model.addAttribute(BLOG_ATTR, blogResponse);
    model.addAttribute("category", categoryName);
    model.addAttribute(PAGEABLE_ATTR, pageable);

    return "blog/main/category-posts";
  }

  /**
   * Tag post page.
   *
   * @param request HttpServletRequest
   * @param username username
   * @param blogUrl  blog url
   * @param tagName  tag name
   * @param pageable pageable
   * @param model    model
   * @return category page
   */
  @Visitant
  @GetMapping("/@{username}/{blogUrl}/tags/{tagName}")
  public String tagPosts(HttpServletRequest request,
                         @PathVariable("username") String username,
                         @PathVariable("blogUrl") String blogUrl,
                         @PathVariable("tagName") String tagName,
                         @PageableDefault Pageable pageable,
                         Model model) {
    String blogKey = getBlogKey(username, blogUrl);

    GetBlogResponseDto blogResponse = blogService.getBlogByUrl(blogKey);

    model.addAttribute(BLOG_ATTR, blogResponse);
    model.addAttribute("tag", tagName);
    model.addAttribute(PAGEABLE_ATTR, pageable);

    return "blog/main/tag-posts";
  }
}
