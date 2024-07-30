package com.blogitory.blog.member.service;

import com.blogitory.blog.member.dto.request.MemberLoginRequestDto;
import com.blogitory.blog.member.dto.request.MemberSignupRequestDto;
import com.blogitory.blog.member.dto.request.MemberUpdateProfileRequestDto;
import com.blogitory.blog.member.dto.request.UpdatePasswordRequestDto;
import com.blogitory.blog.member.dto.response.MemberPersistInfoDto;
import com.blogitory.blog.member.dto.response.MemberProfileResponseDto;
import com.blogitory.blog.member.dto.response.MemberSettingsAlertResponseDto;
import com.blogitory.blog.member.dto.response.MemberSettingsProfileResponseDto;

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

  /**
   * Get user profile for settings page.
   *
   * @param memberNo member no
   * @return responses
   */
  MemberSettingsProfileResponseDto getSettingsProfile(Integer memberNo);

  /**
   * Get user alert settings.
   *
   * @param memberNo member no
   * @return responses
   */
  MemberSettingsAlertResponseDto getSettingsAlert(Integer memberNo);

  /**
   * Modify user alerts.
   *
   * @param memberNo member no
   * @param type     type
   * @param isOn     is on or off
   */
  void updateAlerts(Integer memberNo, String type, boolean isOn);

  /**
   * Modify user password.
   *
   * @param requestDto new password
   */
  void updatePassword(UpdatePasswordRequestDto requestDto);

  /**
   * Quit user.
   *
   * @param memberNo member no
   * @param pwd      password
   */
  void deleteUser(Integer memberNo, String pwd);
}
