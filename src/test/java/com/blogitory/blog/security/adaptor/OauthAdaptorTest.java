package com.blogitory.blog.security.adaptor;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.blogitory.blog.commons.properties.GithubOauthProperties;
import com.blogitory.blog.security.dto.GithubAccessTokenResponseDto;
import com.blogitory.blog.security.dto.GithubUserInfoResponseDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

/**
 * Oauth adaptor test.
 *
 * @author woonseok
 * @Date 2024-08-23
 * @since 1.0
 **/
class OauthAdaptorTest {
  private OauthAdaptor oauthAdaptor;
  private RestTemplate restTemplate;
  private GithubOauthProperties githubOauthProperties;

  @BeforeEach
  void setUp() {
    restTemplate = mock(RestTemplate.class);
    githubOauthProperties = mock(GithubOauthProperties.class);

    oauthAdaptor = new OauthAdaptorImpl(restTemplate, githubOauthProperties);
  }

  @Test
  @DisplayName("Github access token 발급")
  void getGithubAccessToken() {
    GithubAccessTokenResponseDto responseDto = new GithubAccessTokenResponseDto();
    ReflectionTestUtils.setField(responseDto, "accessToken", "access_token");
    ReflectionTestUtils.setField(responseDto, "tokenType", "tokenType");
    ReflectionTestUtils.setField(responseDto, "scope", "scope");

    when(githubOauthProperties.getClientId()).thenReturn("clientId");
    when(githubOauthProperties.getClientSecret()).thenReturn("clientSecret");
    when(restTemplate.exchange(anyString(), any(), any(), eq(GithubAccessTokenResponseDto.class)))
            .thenReturn(ResponseEntity.ok(responseDto));

    GithubAccessTokenResponseDto result = oauthAdaptor.getGithubAccessToken("code");

    assertEquals(responseDto.getAccessToken(), result.getAccessToken());
    assertEquals(responseDto.getTokenType(), result.getTokenType());
    assertEquals(responseDto.getScope(), result.getScope());
  }

  @Test
  @DisplayName("Github 유저 정보 요청")
  void getGithubUserInfo() {
    GithubUserInfoResponseDto responseDto = new GithubUserInfoResponseDto();
    ReflectionTestUtils.setField(responseDto, "login", "login");
    ReflectionTestUtils.setField(responseDto, "id", "id");
    ReflectionTestUtils.setField(responseDto, "avatarUrl", "avatarUrl");
    ReflectionTestUtils.setField(responseDto, "name", "name");

    when(restTemplate.exchange(anyString(), any(), any(), eq(GithubUserInfoResponseDto.class)))
            .thenReturn(ResponseEntity.ok(responseDto));

    GithubUserInfoResponseDto result = oauthAdaptor.getGithubUserInfo("accessToken");

    assertEquals(responseDto.getLogin(), result.getLogin());
    assertEquals(responseDto.getId(), result.getId());
    assertEquals(responseDto.getAvatarUrl(), result.getAvatarUrl());
  }
}