package com.blogitory.blog.member.service;

import com.blogitory.blog.member.dto.MemberLoginRequestDto;
import com.blogitory.blog.member.dto.MemberMyProfileResponseDto;
import com.blogitory.blog.member.dto.MemberPersistInfoDto;
import com.blogitory.blog.member.dto.MemberSignupRequestDto;
import com.blogitory.blog.member.dto.MemberUpdateNameRequestDto;
import com.blogitory.blog.member.dto.MemberUpdateProfileRequestDto;
import org.springframework.transaction.annotation.Transactional;

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
   * Get profile login user.
   *
   * @param memberNo user no
   * @return profile info
   */
  MemberMyProfileResponseDto myProfile(Integer memberNo);

  /**
   * Get user info for header.
   *
   * @param memberNo user no
   * @return user info
   */
  MemberPersistInfoDto persistInfo(Integer memberNo);

  /**
   * Update username.
   *
   * @param memberNo   user no
   * @param requestDto new username
   */
  void updateName(Integer memberNo, MemberUpdateNameRequestDto requestDto);

  /**
   * Update open email.
   *
   * @param memberNo   user no
   * @param requestDto new email
   */
  void updateOpenEmail(Integer memberNo, MemberUpdateProfileRequestDto requestDto);

  /**
   * Update github url.
   *
   * @param memberNo   user no
   * @param requestDto github url
   */
  void updateGithub(Integer memberNo, MemberUpdateProfileRequestDto requestDto);

  /**
   * Update facebook url.
   *
   * @param memberNo   user no
   * @param requestDto new facebook url
   */
  void updateFacebook(Integer memberNo, MemberUpdateProfileRequestDto requestDto);

  /**
   * Update x url.
   *
   * @param memberNo   user no
   * @param requestDto new x url
   */
  void updateX(Integer memberNo, MemberUpdateProfileRequestDto requestDto);

  /**
   * Update homepage url.
   *
   * @param memberNo   user no
   * @param requestDto new homepage url
   */
  void updateHomepage(Integer memberNo, MemberUpdateProfileRequestDto requestDto);

  @Transactional
  void updateBio(Integer memberNo, MemberUpdateProfileRequestDto requestDto);

  Boolean isDuplicateUsername(String username);

  String getPasswordByEmail(String email);

  String getThumbnailByNo(Integer memberNo);
}
