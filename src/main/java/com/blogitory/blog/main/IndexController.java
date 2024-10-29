package com.blogitory.blog.main;

import com.blogitory.blog.blog.dto.response.GetBlogInSettingsResponseDto;
import com.blogitory.blog.blog.service.BlogService;
import com.blogitory.blog.commons.annotaion.RoleAnonymous;
import com.blogitory.blog.commons.annotaion.RoleUser;
import com.blogitory.blog.member.service.MemberService;
import com.blogitory.blog.posts.service.PostsService;
import com.blogitory.blog.security.util.SecurityUtils;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Controller for index page.
 *
 * @author woonseok
 * @since 1.0
 **/
@Slf4j
@Controller
@RequiredArgsConstructor
public class IndexController {
  private final BlogService blogService;
  private final MemberService memberService;
  private final PostsService postsService;

  /**
   * Go to main page.
   *
   * @return main page
   */
  @GetMapping("/")
  public String indexPage() {
    return "index/main/index";
  }

  /**
   * Go to signup page.
   *
   * @return signup page
   */
  @RoleAnonymous
  @GetMapping("/signup")
  public String signupPage() {
    return "index/main/signup";
  }

  /**
   * Go to shuffle page.
   *
   * @return shuffle page
   */
  @RoleUser
  @GetMapping("/feed")
  public String feedPage() {
    return "index/main/feed";
  }

  /**
   * Go to Settings blog page.
   *
   * @return Settings blog page
   */
  @RoleUser
  @GetMapping("/settings/blog")
  public String blogSettings(Model model) {
    List<GetBlogInSettingsResponseDto> blogs =
            blogService.getBlogListByMemberNo(SecurityUtils.getCurrentUserNo());

    if (blogs.isEmpty()) {
      model.addAttribute("noBlog", true);
      return "index/settings/blog";
    }

    model.addAttribute("noBlog", false);
    model.addAttribute("blogs", blogs);

    return "index/settings/blog";
  }

  /**
   * Go to settings page.
   *
   * @param model model
   * @return settings page
   */
  @RoleUser
  @GetMapping("/settings")
  public String settings(Model model) {
    Integer memberNo = SecurityUtils.getCurrentUserNo();

    model.addAttribute("memberProfile", memberService.getSettingsProfile(memberNo));
    return "index/settings/index";
  }

  /**
   * Go to notification settings page.
   *
   * @param model model
   * @return settings page
   */
  @RoleUser
  @GetMapping("/settings/notification")
  public String notificationSettings(Model model) {
    Integer memberNo = SecurityUtils.getCurrentUserNo();

    model.addAttribute("alerts", memberService.getSettingsAlert(memberNo));
    return "index/settings/notice";
  }

  /**
   * Go to posts settings page.
   *
   * @param pageable pageable
   * @param model    model
   * @return settings page
   */
  @RoleUser
  @GetMapping("/settings/posts")
  public String postSettings(@PageableDefault Pageable pageable, Model model) {
    Integer memberNo = SecurityUtils.getCurrentUserNo();

    model.addAttribute("posts", postsService.getPostsByMemberNo(pageable, memberNo));

    return "index/settings/posts";
  }
}

