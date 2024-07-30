package com.blogitory.blog.main;

import com.blogitory.blog.blog.dto.response.BlogListInSettingsResponseDto;
import com.blogitory.blog.blog.service.BlogService;
import com.blogitory.blog.commons.annotaion.RoleAnonymous;
import com.blogitory.blog.commons.annotaion.RoleUser;
import com.blogitory.blog.member.service.MemberService;
import com.blogitory.blog.security.util.SecurityUtils;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

  @GetMapping("/shuffle")
  public String shufflePage() {
    return "index/main/shuffle";
  }

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
    List<BlogListInSettingsResponseDto> blogs =
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
  public String settingsPage(Model model) {
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
  public String notificationPage(Model model) {
    Integer memberNo = SecurityUtils.getCurrentUserNo();

    model.addAttribute("alerts", memberService.getSettingsAlert(memberNo));
    return "index/settings/notice";
  }
}

