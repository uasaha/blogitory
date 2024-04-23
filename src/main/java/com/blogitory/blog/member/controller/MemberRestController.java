package com.blogitory.blog.member.controller;

import static com.blogitory.blog.security.util.JwtUtils.ACCESS_TOKEN_COOKIE_NAME;

import com.blogitory.blog.member.dto.MemberLoginRequestDto;
import com.blogitory.blog.member.dto.MemberUpdateNameRequestDto;
import com.blogitory.blog.member.dto.MemberUpdateProfileRequestDto;
import com.blogitory.blog.member.service.MemberService;
import com.blogitory.blog.security.util.SecurityUtils;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Member Rest api Controller.
 *
 * @author woonseok
 * @since 1.0
 **/
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class MemberRestController {
  private final MemberService memberService;

  /**
   * Login for user using rest services.
   *
   * @param response   HttpServletResponse
   * @param requestDto login info
   * @return JWT
   */
  @PostMapping("/login")
  public ResponseEntity<String> login(HttpServletResponse response,
                                      @RequestBody @Valid MemberLoginRequestDto requestDto) {
    String accessToken = memberService.login(requestDto);

    Cookie cookie = new Cookie(ACCESS_TOKEN_COOKIE_NAME, accessToken);

    response.addCookie(cookie);

    return ResponseEntity.ok(accessToken);
  }

  /**
   * Update Profile name.
   *
   * @param requestDto new name
   * @return is done
   */
  @PutMapping("/profile/name")
  public ResponseEntity<Void> updateName(
          @RequestBody @Valid MemberUpdateNameRequestDto requestDto) {
    Integer memberNo = SecurityUtils.getCurrentUserNo();

    memberService.updateName(memberNo, requestDto);

    return ResponseEntity.noContent().build();
  }

  /**
   * Update Profile open email.
   *
   * @param requestDto new open email
   * @return is done
   */
  @PutMapping("/profile/open-email")
  public ResponseEntity<Void> updateOpenEmail(
          @RequestBody @Valid MemberUpdateProfileRequestDto requestDto) {
    Integer memberNo = SecurityUtils.getCurrentUserNo();

    memberService.updateOpenEmail(memberNo, requestDto);

    return ResponseEntity.noContent().build();
  }

  /**
   * Update Profile github url.
   *
   * @param requestDto new github url
   * @return is done
   */
  @PutMapping("/profile/github")
  public ResponseEntity<Void> updateGithub(
          @RequestBody @Valid MemberUpdateProfileRequestDto requestDto) {
    Integer memberNo = SecurityUtils.getCurrentUserNo();

    memberService.updateGithub(memberNo, requestDto);

    return ResponseEntity.noContent().build();
  }

  /**
   * Update Profile facebook url.
   *
   * @param requestDto new facebook url
   * @return is done
   */
  @PutMapping("/profile/facebook")
  public ResponseEntity<Void> updateFacebook(
          @RequestBody @Valid MemberUpdateProfileRequestDto requestDto) {
    Integer memberNo = SecurityUtils.getCurrentUserNo();

    memberService.updateFacebook(memberNo, requestDto);

    return ResponseEntity.noContent().build();
  }

  /**
   * Update Profile X url.
   *
   * @param requestDto new X url
   * @return is done
   */
  @PutMapping("/profile/x")
  public ResponseEntity<Void> updateX(
          @RequestBody @Valid MemberUpdateProfileRequestDto requestDto) {
    Integer memberNo = SecurityUtils.getCurrentUserNo();

    memberService.updateX(memberNo, requestDto);

    return ResponseEntity.noContent().build();
  }

  /**
   * Update Profile homepage url.
   *
   * @param requestDto new homepage url
   * @return is done
   */
  @PutMapping("/profile/homepage")
  public ResponseEntity<Void> updateHomepage(
          @RequestBody @Valid MemberUpdateProfileRequestDto requestDto) {
    Integer memberNo = SecurityUtils.getCurrentUserNo();

    memberService.updateHomepage(memberNo, requestDto);

    return ResponseEntity.noContent().build();
  }
}
