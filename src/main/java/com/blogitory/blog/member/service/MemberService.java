package com.blogitory.blog.member.service;

import com.blogitory.blog.member.dto.MemberSignupRequestDto;

/**
 * Service of Member.
 *
 * @author woonseok
 * @since 1.0
 **/
public interface MemberService {
  /**
   * Signup member.
   *
   * @param requestDto Signup info.
   */
  void signup(MemberSignupRequestDto requestDto);

  /**
   * Already used email, return true
   * or not, return false.
   *
   * @param email new email
   * @return email existence
   */
  boolean existMemberByEmail(String email);
}
