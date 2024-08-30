package com.blogitory.blog.member.controller;

import com.blogitory.blog.commons.annotaion.RoleAnonymous;
import com.blogitory.blog.follow.service.FollowService;
import com.blogitory.blog.member.dto.request.SignupMemberRequestDto;
import com.blogitory.blog.member.dto.request.UpdatePasswordRequestDto;
import com.blogitory.blog.member.dto.response.GetMemberProfileResponseDto;
import com.blogitory.blog.member.exception.MemberPwdChangeFailedException;
import com.blogitory.blog.member.service.MemberService;
import com.blogitory.blog.security.util.SecurityUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
  private final FollowService followService;

  /**
   * Signup Member.
   *
   * @param requestDto Signup info
   * @return index page
   */
  @RoleAnonymous
  @PostMapping("/signup")
  public String signup(@Valid SignupMemberRequestDto requestDto) {
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
    GetMemberProfileResponseDto profile = memberService.getProfileByUsername(username);

    model.addAttribute("profile", profile);

    if (SecurityUtils.isAuthenticated()) {
      Integer memberNo = SecurityUtils.getCurrentUserNo();
      model.addAttribute("isFollowed", followService.isFollowed(memberNo, username));
    }

    return "profile/main/index";
  }

  /**
   * Followers page.
   *
   * @param username username
   * @param model    model
   * @return Followers view
   */
  @GetMapping("/@{username}/follows")
  public String follows(@PathVariable String username, Model model) {
    GetMemberProfileResponseDto profile = memberService.getProfileByUsername(username);

    model.addAttribute("profile", profile);

    Integer memberNo = null;

    if (SecurityUtils.isAuthenticated()) {
      memberNo = SecurityUtils.getCurrentUserNo();
      model.addAttribute("isFollowed", followService.isFollowed(memberNo, username));
    }

    model.addAttribute("followInfo", followService.getAllFollowInfo(memberNo, username));

    return "profile/main/follow";
  }

  /**
   * Go to modify password page.
   *
   * @param model      model
   * @param passwordId password id
   * @return password page
   */
  @GetMapping("/users/passwords")
  public String modifyPassword(Model model, @RequestParam("ui") String passwordId) {
    model.addAttribute("ui", passwordId);

    return "index/main/passwordUpdate";
  }

  /**
   * Modify password.
   *
   * @param requestDto new password
   * @return success page
   */
  @PostMapping("/users/passwords")
  public String doModifyPassword(@Valid UpdatePasswordRequestDto requestDto) {
    memberService.updatePassword(requestDto);

    return "redirect:/users/passwords/su";
  }

  /**
   * Go to Modify password success page.
   *
   * @return success page
   */
  @GetMapping("/users/passwords/su")
  public String modifyPasswordSuccess() {
    return "/index/main/passwordUpdateSuccess";
  }

  /**
   * Password modify failed page.
   *
   * @return failed page
   */
  @ExceptionHandler(value = {MemberPwdChangeFailedException.class})
  public String modifyPasswordError() {
    return "/index/main/passwordUpdateFailed";
  }
}