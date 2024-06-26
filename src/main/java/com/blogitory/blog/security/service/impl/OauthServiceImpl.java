package com.blogitory.blog.security.service.impl;

import com.blogitory.blog.security.adaptor.OauthAdaptor;
import com.blogitory.blog.security.dto.GithubAccessTokenResponseDto;
import com.blogitory.blog.security.exception.AuthenticationException;
import com.blogitory.blog.security.service.OauthService;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * Oauth Service.
 *
 * @author woonseok
 * @since 1.0
 **/
@Service
@RequiredArgsConstructor
public class OauthServiceImpl implements OauthService {

  private final OauthAdaptor oauthAdaptor;

  /**
   * {@inheritDoc}
   */
  @Override
  public String getGithubAccessToken(String code) {
    GithubAccessTokenResponseDto accessTokenResponseDto = oauthAdaptor.getGithubAccessToken(code);

    if (Objects.isNull(accessTokenResponseDto)) {
      throw new AuthenticationException("Github access token response is null.");
    }

    return accessTokenResponseDto.getAccessToken();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String githubLogin(String githubAccessToken) {

    return null;
  }
}