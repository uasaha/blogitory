package com.blogitory.blog.member.controller;

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


  /**
   * Checking for Is Username Duplicated due register.
   *
   * @param username username
   * @return isDuplicated
   */
  @GetMapping("/username/verification")
  public ResponseEntity<Boolean> isDuplicatedUsername(
          @RequestParam @Valid @Size(min = 2, max = 20) String username) {
    return ResponseEntity.ok(memberService.isDuplicateUsername(username));
  }

  /**
   * User update profile.
   *
   * @param requestDto request body
   * @return isUpdated
   */
  @PutMapping("/profiles")
  public ResponseEntity<Void> updateProfile(@RequestBody @Valid MemberUpdateProfileRequestDto requestDto) {
    Integer memberNo = SecurityUtils.getCurrentUserNo();

    memberService.updateProfile(memberNo, requestDto);

    return ResponseEntity.noContent().build();
  }
}
