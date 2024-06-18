package com.blogitory.blog.main;

import com.blogitory.blog.blog.dto.BlogListInSettingsResponseDto;
import com.blogitory.blog.blog.service.BlogService;
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
  private final MemberService memberService;
  private final BlogService blogService;

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
  @GetMapping("/signup")
  public String signupPage() {
    return "index/main/signup";
  }

  /**
   * Go to Settings page.
   *
   * @param model Model
   * @return Settings page
   */
  @RoleUser
  @GetMapping("/settings")
  public String settings(Model model) {
    Integer memberNo = SecurityUtils.getCurrentUserNo();

    model.addAttribute("profiles",
            memberService.myProfile(memberNo));

    return "index/settings/index";
  }

  /**
   * Go to Settings profile page.
   *
   * @param model Model
   * @return Settings profile page
   */
  @RoleUser
  @GetMapping("/settings/profile")
  public String profileSettings(Model model) {
    Integer memberNo = SecurityUtils.getCurrentUserNo();

    model.addAttribute("profiles",
            memberService.myProfile(memberNo));

    return "index/settings/profile";
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
}

