package com.blogitory.blog.security.service;

import com.blogitory.blog.member.dto.request.SignupOauthMemberRequestDto;

/**
 * Oauth Service.
 *
 * @author woonseok
 * @since 1.0
 **/
public interface OauthService {

  void oauthSignup(SignupOauthMemberRequestDto requestDto);

  /**
   * Get GitHub access token.
   *
   * @param code verification code
   * @return GitHub access token
   */
  String getGithubAccessToken(String code);

  /**
   * Login with GitHub.
   *
   * @param githubAccessToken GitHub Access Token
   * @return user's info
   */
  String githubLogin(String githubAccessToken);
}
