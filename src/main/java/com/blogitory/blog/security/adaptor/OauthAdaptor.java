package com.blogitory.blog.security.adaptor;

import com.blogitory.blog.commons.properties.GithubOauthProperties;
import com.blogitory.blog.security.dto.GithubAccessTokenResponseDto;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

/**
 * OauthAdaptor for getting user's info with Oauth.
 *
 * @author woonseok
 * @since 1.0
 **/
@Component
@RequiredArgsConstructor
public class OauthAdaptor {
  private final RestTemplate restTemplate;
  private final GithubOauthProperties githubOauthProperties;

  /**
   * Get GitHub access Token.
   *
   * @param code verification code
   * @return GitHub access token
   */
  public GithubAccessTokenResponseDto getGithubAccessToken(String code) {
    final String gitAccessTokenUrl = "https://github.com/login/oauth/access_token";
    String finalUrl = gitAccessTokenUrl
            + "?client_id=" + githubOauthProperties.getClientId()
            + "&client_secret=" + githubOauthProperties.getClientSecret()
            + "&code=" + code;

    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    headers.setAccept(List.of(MediaType.APPLICATION_JSON));

    return Objects.requireNonNull(restTemplate.exchange(
            finalUrl,
            HttpMethod.POST,
            new HttpEntity<>(headers),
            GithubAccessTokenResponseDto.class).getBody());
  }
}
