package com.blogitory.blog.member.controller;

import com.blogitory.blog.member.dto.MemberSignupRequestDto;
import com.blogitory.blog.member.service.MemberService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * The Controller for Member's page.
 *
 * @author woonseok
 * @since 1.0
 **/
@Controller
@RequiredArgsConstructor
public class MemberController {
  private final MemberService memberService;

  private static final String REFERER = "Referer";

  /**
   * Signup Member.
   *
   * @param requestDto Signup info
   * @return index page
   */
  @PostMapping("/signup")
  public String signup(HttpServletRequest request,
                       @Valid MemberSignupRequestDto requestDto) {
    memberService.signup(requestDto);

    return "redirect:/";
  }
}
