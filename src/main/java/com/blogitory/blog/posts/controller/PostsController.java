package com.blogitory.blog.posts.controller;

import static com.blogitory.blog.blog.controller.BlogController.PAGEABLE_ATTR;
import static com.blogitory.blog.commons.utils.UrlUtil.getBlogKey;
import static com.blogitory.blog.commons.utils.UrlUtil.getPostsKey;

import com.blogitory.blog.blog.dto.response.GetBlogResponseDto;
import com.blogitory.blog.blog.dto.response.GetBlogWithCategoryResponseDto;
import com.blogitory.blog.blog.service.BlogService;
import com.blogitory.blog.commons.annotaion.RoleUser;
import com.blogitory.blog.follow.service.FollowService;
import com.blogitory.blog.posts.dto.request.SaveTempPostsDto;
import com.blogitory.blog.posts.dto.response.GetBeforeNextPostsResponseDto;
import com.blogitory.blog.posts.dto.response.GetPostForModifyResponseDto;
import com.blogitory.blog.posts.dto.response.GetPostResponseDto;
import com.blogitory.blog.posts.service.PostsService;
import com.blogitory.blog.security.util.SecurityUtils;
import com.blogitory.blog.viewer.aspect.point.Viewer;
import com.blogitory.blog.viewer.service.ViewerService;
import com.blogitory.blog.visitant.aspect.point.Visitant;
import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Posts controller.
 *
 * @author woonseok
 * @since 1.0
 **/
@Controller
@Slf4j
@RequiredArgsConstructor
public class PostsController {
  private final BlogService blogService;
  private final PostsService postsService;
  private final FollowService followService;
  private final ViewerService viewerService;
  private static final String POSTS_ATTR = "posts";

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

    List<GetBlogWithCategoryResponseDto> blogs = blogService.getBlogListWithCategory(memberNo);

    if (blogs.isEmpty()) {
      return "redirect:/settings/blog";
    }

    SaveTempPostsDto saveDto = postsService.loadTempPosts(tp, memberNo);

    model.addAttribute("blogs", blogs);
    model.addAttribute(POSTS_ATTR, saveDto);

    return "index/posts/write";
  }

  /**
   * Go to modify posts page.
   *
   * @param username username
   * @param blogUrl  blog url
   * @param postUrl  post url
   * @param model    model
   * @return page
   */
  @RoleUser
  @GetMapping("/@{username}/{blogUrl}/{postUrl}/mod")
  public String modifyPostsPage(
          @PathVariable String username,
          @PathVariable String blogUrl,
          @PathVariable String postUrl,
          Model model) {
    String postKey = getPostsKey(username, blogUrl, postUrl);
    Integer memberNo = SecurityUtils.getCurrentUserNo();

    GetPostForModifyResponseDto response = postsService.getPostForModifyByUrl(memberNo, postKey);

    model.addAttribute(POSTS_ATTR, response);

    return "index/posts/modify";
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
  @Visitant
  @Viewer
  @GetMapping("/@{username}/{blogUrl}/{postUrl}")
  public String postsPage(HttpServletRequest request,
                          @PathVariable String username,
                          @PathVariable String blogUrl,
                          @PathVariable String postUrl,
                          Model model) {
    String blogKey = getBlogKey(username, blogUrl);
    String postKey = getPostsKey(username, blogUrl, postUrl);

    GetBlogResponseDto blogResponse = blogService.getBlogByUrl(blogKey);
    GetPostResponseDto postsResponse = postsService.getPostByUrl(postKey);
    GetBeforeNextPostsResponseDto relatedPosts = postsService.getRelatedPosts(postKey);

    model.addAttribute("relatedPosts", relatedPosts);
    model.addAttribute("blog", blogResponse);
    model.addAttribute(POSTS_ATTR, postsResponse);

    if (SecurityUtils.isAuthenticated()) {
      Integer memberNo = SecurityUtils.getCurrentUserNo();
      model.addAttribute("isFollowed", followService.isFollowed(memberNo, username));
    }

    return "blog/main/posts";
  }

  /**
   * Go to hearts posts page.
   *
   * @param pageable pageable
   * @param model    model
   * @return hearts posts page
   */
  @RoleUser
  @GetMapping("/hearts")
  public String hearts(@PageableDefault(size = 12) Pageable pageable, Model model) {
    model.addAttribute(PAGEABLE_ATTR, pageable);
    return "index/posts/hearts";
  }

  /**
   * Go to search page.
   *
   * @param keyword keyword
   * @param model   model
   * @return search page
   */
  @GetMapping("/search")
  public String searchPage(@RequestParam(name = "q", required = false) String keyword,
                           Model model) {

    if (!Objects.isNull(keyword)) {
      model.addAttribute("keyword", keyword);
    }

    return "index/main/search";
  }

  /**
   * Viewer page.
   *
   * @param username username
   * @param blogUrl  blog url
   * @param postUrl  post url
   * @param model    model
   * @return viewer page
   */
  @RoleUser
  @GetMapping("/@{username}/{blogUrl}/{postUrl}/viewers")
  public String viewerPage(@PathVariable String username,
                           @PathVariable String blogUrl,
                           @PathVariable String postUrl,
                           Model model) {
    Integer memberNo = SecurityUtils.getCurrentUserNo();
    String postKey = getPostsKey(username, blogUrl, postUrl);

    model.addAttribute(POSTS_ATTR, postsService.getPostByUrl(postKey));
    model.addAttribute("total", viewerService.getViewersCount(memberNo, postKey));

    return "chart/posts";
  }
}
