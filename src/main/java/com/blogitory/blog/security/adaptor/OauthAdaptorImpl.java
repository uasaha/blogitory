package com.blogitory.blog.security.adaptor;

import com.blogitory.blog.commons.properties.GithubOauthProperties;
import com.blogitory.blog.security.dto.GithubAccessTokenResponseDto;
import com.blogitory.blog.security.dto.GithubUserInfoResponseDto;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

/**
 * Implementation of OauthAdaptor.
 *
 * @author woonseok
 * @since 1.0
 **/
@Component
@Slf4j
@RequiredArgsConstructor
public class OauthAdaptorImpl implements OauthAdaptor {
  private final RestTemplate restTemplate;
  private final GithubOauthProperties githubOauthProperties;

  private static final String GIT_ISSUE_TOKEN_URL = "https://github.com/login/oauth/access_token";
  private static final String GIT_USER_URL = "https://api.github.com/user";
  private static final String AUTHORIZATION = "Authorization";
  private static final String TOKEN_TYPE = "Bearer ";

  /**
   * {@inheritDoc}
   */
  @Override
  public GithubAccessTokenResponseDto getGithubAccessToken(String code) {
    String url = GIT_ISSUE_TOKEN_URL
            + "?client_id=" + githubOauthProperties.getClientId()
            + "&client_secret=" + githubOauthProperties.getClientSecret()
            + "&code=" + code;

    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    headers.setAccept(List.of(MediaType.APPLICATION_JSON));

    ResponseEntity<GithubAccessTokenResponseDto> response = restTemplate.exchange(
            url,
            HttpMethod.POST,
            new HttpEntity<>(headers),
            GithubAccessTokenResponseDto.class);


    return Objects.requireNonNull(response.getBody());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public GithubUserInfoResponseDto getGithubUserInfo(String githubAccessToken) {
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    headers.setAccept(List.of(MediaType.APPLICATION_JSON));
    headers.add(AUTHORIZATION, TOKEN_TYPE + githubAccessToken);

    ResponseEntity<GithubUserInfoResponseDto> response = restTemplate.exchange(
            GIT_USER_URL,
            HttpMethod.GET,
            new HttpEntity<>(headers),
            GithubUserInfoResponseDto.class);

    return Objects.requireNonNull(response.getBody());
  }
}
