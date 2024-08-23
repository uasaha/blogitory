package com.blogitory.blog.security.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.blogitory.blog.blog.service.BlogService;
import com.blogitory.blog.commons.properties.GithubOauthProperties;
import com.blogitory.blog.commons.utils.CookieUtils;
import com.blogitory.blog.config.TestSecurityConfig;
import com.blogitory.blog.security.exception.NotFoundOauthUser;
import com.blogitory.blog.security.service.OauthService;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

/**
 * Oauth controller test.
 *
 * @author woonseok
 * @Date 2024-08-23
 * @since 1.0
 **/
@WebMvcTest(value = {OauthController.class, TestSecurityConfig.class})
class OauthControllerTest {

  @Autowired
  MockMvc mvc;

  @Autowired
  ObjectMapper objectMapper;

  @MockBean
  BlogService blogService;

  @MockBean
  OauthService oauthService;

  @MockBean
  GithubOauthProperties githubOauthProperties;

  @MockBean
  CookieUtils cookieUtils;

  @Test
  @DisplayName("깃허브 로그인 페이지로 이동")
  void goToGithub() throws Exception {
    when(githubOauthProperties.getClientId()).thenReturn("clientId");

    mvc.perform(get("/login/oauth/github/auth"))
            .andExpect(status().is3xxRedirection());
  }

  @Test
  @DisplayName("깃허브 로그인")
  void githubOauthLogin() throws Exception {
    String githubAccessToken = "githubAccessToken";
    String accessToken = "accessToken";

    when(oauthService.getGithubAccessToken(anyString())).thenReturn(githubAccessToken);
    when(oauthService.githubLogin(any())).thenReturn(accessToken);
    doNothing().when(cookieUtils).addSecureCookie(any(), anyString(), anyString(), any());

    mvc.perform(get("/login/oauth/github")
                    .param("code", "code"))
            .andExpect(status().is3xxRedirection());
  }

  @Test
  @DisplayName("소셜 회원가입")
  void signupOauth() throws Exception {
    doNothing().when(oauthService).oauthSignup(any());

    mvc.perform(post("/signup/oauth")
                    .param("provider", "provider")
                    .param("id", "id")
                    .param("profileThumb", "profileThumb")
                    .param("username", "username")
                    .param("name", "name"))
            .andExpect(status().is3xxRedirection());
  }

  @Test
  @DisplayName("회원을 못찾을 때 회원가입 페이지로 이동")
  void notFoundOauthUser() throws Exception {
    String githubAccessToken = "githubAccessToken";

    when(oauthService.getGithubAccessToken(anyString())).thenReturn(githubAccessToken);
    when(oauthService.githubLogin(any())).thenThrow(NotFoundOauthUser.class);

    Map<String, Object> attrs = new HashMap<>();
    attrs.put("provider", "provider");
    attrs.put("id", "id");
    attrs.put("name", "name");
    attrs.put("profileThumb", "profileThumb");
    attrs.put("username", "username");

    mvc.perform(get("/login/oauth/github")
            .param("code", "code")
            .flashAttrs(attrs))
            .andExpect(status().isOk());
  }
}