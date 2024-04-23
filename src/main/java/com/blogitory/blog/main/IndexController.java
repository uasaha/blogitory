package com.blogitory.blog.main;

import com.blogitory.blog.commons.annotaion.RoleUser;
import com.blogitory.blog.member.service.MemberService;
import com.blogitory.blog.security.util.SecurityUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
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
   * User Logout.
   *
   * @param request  HttpServletRequest
   * @param response HttpServletResponse
   * @return Index page
   */
  @GetMapping("/logout")
  public String logout(HttpServletRequest request, HttpServletResponse response) {

    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication != null) {
      new SecurityContextLogoutHandler().logout(request, response, authentication);
    }

    return "redirect:/";
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
  public String blogSettings() {
    return "index/settings/blog";
  }
}

