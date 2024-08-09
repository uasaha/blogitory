package com.blogitory.blog.member.controller;

import static com.blogitory.blog.security.util.JwtUtils.ACCESS_TOKEN_COOKIE_NAME;
import static com.blogitory.blog.security.util.JwtUtils.makeSecureCookie;

import com.blogitory.blog.commons.annotaion.RoleUser;
import com.blogitory.blog.mail.service.MailService;
import com.blogitory.blog.member.dto.request.LeftMemberRequestDto;
import com.blogitory.blog.member.dto.request.UpdateMemberProfileRequestDto;
import com.blogitory.blog.member.service.MemberService;
import com.blogitory.blog.security.util.SecurityUtils;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Member Rest api Controller.
 *
 * @author woonseok
 * @since 1.0
 **/
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class MemberRestController {
  private final MemberService memberService;
  private final MailService mailService;


  /**
   * Checking for Is Username Duplicated due register.
   *
   * @param username username
   * @return isDuplicated
   */
  @GetMapping("/username/verification")
  public ResponseEntity<Boolean> isDuplicatedUsername(
          @RequestParam @Valid @Size(min = 2, max = 20)
          @Pattern(regexp = "^[a-zA-Z0-9-]+$") String username) {
    return ResponseEntity.ok(memberService.isDuplicateUsername(username));
  }

  /**
   * User update profile.
   *
   * @param requestDto request body
   * @return isUpdated
   */
  @PutMapping("/profiles")
  public ResponseEntity<Void> updateProfile(
          @RequestBody @Valid UpdateMemberProfileRequestDto requestDto) {
    Integer memberNo = SecurityUtils.getCurrentUserNo();

    memberService.updateProfile(memberNo, requestDto);

    return ResponseEntity.noContent().build();
  }

  /**
   * Modify alerts setting.
   *
   * @param type alert type
   * @param isOn is on or off
   * @return 204
   */
  @PutMapping("/alerts")
  public ResponseEntity<Void> updateAlerts(
          @RequestParam String type, @RequestParam boolean isOn) {
    Integer memberNo = SecurityUtils.getCurrentUserNo();

    memberService.updateAlerts(memberNo, type, isOn);

    return ResponseEntity.noContent().build();
  }

  /**
   * Send modify password page mail.
   *
   * @param email user email
   * @return 204
   */
  @GetMapping("/password")
  public ResponseEntity<Void> sendPasswordUpdateMail(@RequestParam String email) {
    mailService.sendUpdatePassword(email);

    return ResponseEntity.noContent().build();
  }

  /**
   * Quit user.
   *
   * @param response   204
   * @param requestDto password
   * @return 204
   */
  @RoleUser
  @PostMapping
  public ResponseEntity<Void> deleteUser(HttpServletResponse response,
                                         @RequestBody LeftMemberRequestDto requestDto) {
    Integer memberNo = SecurityUtils.getCurrentUserNo();

    memberService.deleteUser(memberNo, requestDto.getPassword());

    ResponseCookie cookie = makeSecureCookie(ACCESS_TOKEN_COOKIE_NAME, "", 0);
    response.addHeader("Set-Cookie", cookie.toString());

    return ResponseEntity.noContent().build();
  }
}
