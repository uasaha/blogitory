package com.blogitory.blog.member.service;

import com.blogitory.blog.member.dto.MemberLoginRequestDto;
import com.blogitory.blog.member.dto.MemberPersistInfoDto;
import com.blogitory.blog.member.dto.MemberProfileResponseDto;
import com.blogitory.blog.member.dto.MemberSignupRequestDto;
import com.blogitory.blog.member.dto.MemberUpdateProfileRequestDto;

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

  /**
   * Member login.
   *
   * @param requestDto login info
   * @return JWT
   */
  String login(MemberLoginRequestDto requestDto);

  /**
   * Get user info for header.
   *
   * @param memberNo user no
   * @return user info
   */
  MemberPersistInfoDto persistInfo(Integer memberNo);

  /**
   * Checking for duplication of username.
   *
   * @param username username
   * @return is duplicated
   */
  Boolean isDuplicateUsername(String username);

  /**
   * Get member's password for Login by email.
   *
   * @param email email
   * @return password
   */
  String getPasswordByEmail(String email);

  /**
   * Get pfp by memberNo.
   *
   * @param memberNo memberNo
   * @return pfp URL
   */
  String getThumbnailByNo(Integer memberNo);

  /**
   * Get Profiles by username.
   *
   * @param username username
   * @return Profile content
   */
  MemberProfileResponseDto getProfileByUsername(String username);

  /**
   * Update Profile.
   *
   * @param memberNo   memberNo
   * @param requestDto needed
   */
  void updateProfile(Integer memberNo, MemberUpdateProfileRequestDto requestDto);
}
