package com.blogitory.blog.member.controller;

import com.blogitory.blog.member.dto.MemberProfileResponseDto;
import com.blogitory.blog.member.dto.MemberSignupRequestDto;
import com.blogitory.blog.member.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * The Controller for Member's page.
 *
 * @author woonseok
 * @since 1.0
 **/
@Slf4j
@Controller
@RequiredArgsConstructor
public class MemberController {
  private final MemberService memberService;

  /**
   * Signup Member.
   *
   * @param requestDto Signup info
   * @return index page
   */
  @PostMapping("/signup")
  public String signup(@Valid MemberSignupRequestDto requestDto) {
    memberService.signup(requestDto);

    return "redirect:/";
  }


  /**
   * Get members profile.
   *
   * @param username member username
   * @param model    model
   * @return profile view
   */
  @GetMapping("/@{username}")
  public String profile(@PathVariable String username, Model model) {
    MemberProfileResponseDto profile = memberService.getProfileByUsername(username);

    model.addAttribute("profile", profile);

    return "profile/main/index";
  }
}
