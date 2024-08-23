package com.blogitory.blog.security.adaptor;

import com.blogitory.blog.security.dto.GithubAccessTokenResponseDto;
import com.blogitory.blog.security.dto.GithubUserInfoResponseDto;

/**
 * OauthAdaptor for getting user's info with Oauth.
 *
 * @author woonseok
 * @Date 2024-08-23
 * @since 1.0
 **/
public interface OauthAdaptor {

  /**
   * Get GitHub access Token.
   *
   * @param code verification code
   * @return GitHub access token
   */
  GithubAccessTokenResponseDto getGithubAccessToken(String code);

  /**
   * Get Github user info.
   *
   * @param githubAccessToken GitHub access token
   * @return user info
   */
  GithubUserInfoResponseDto getGithubUserInfo(String githubAccessToken);
}
