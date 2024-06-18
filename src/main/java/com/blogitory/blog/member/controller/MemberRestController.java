package com.blogitory.blog.member.controller;

import com.blogitory.blog.member.dto.MemberUpdateNameRequestDto;
import com.blogitory.blog.member.dto.MemberUpdateProfileRequestDto;
import com.blogitory.blog.member.service.MemberService;
import com.blogitory.blog.security.util.SecurityUtils;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
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
@RequestMapping("/api/v1/users")
public class MemberRestController {
  private final MemberService memberService;

  @GetMapping("/username/verification")
  public ResponseEntity<Boolean> isDuplicatedName(
          @RequestParam @Valid @Size(min = 2, max = 20) String username) {
    return ResponseEntity.ok(memberService.isDuplicateUsername(username));
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
