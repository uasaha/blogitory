package com.blogitory.blog.security.controller;

import static com.blogitory.blog.security.util.JwtUtils.ACCESS_COOKIE_EXPIRE;
import static com.blogitory.blog.security.util.JwtUtils.ACCESS_TOKEN_COOKIE_NAME;

import com.blogitory.blog.commons.utils.CookieUtils;
import com.blogitory.blog.security.service.OauthService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Oauth Controller.
 *
 * @author woonseok
 * @since 1.0
 **/
@Controller
@RequiredArgsConstructor
public class OauthController {

  private final OauthService oauthService;
  private final CookieUtils cookieUtils;

  /**
   * Login with GitHub Oauth.
   *
   * @param code     verification code
   * @param response HttpServletResponse
   * @return main page view
   */
  @GetMapping("/login/oauth/github")
  public String githubOauthLogin(@RequestParam String code, HttpServletResponse response) {
    String githubAccessToken = oauthService.getGithubAccessToken(code);

    String accessToken = oauthService.githubLogin(githubAccessToken);

    cookieUtils.addSecureCookie(
            response, ACCESS_TOKEN_COOKIE_NAME, accessToken, ACCESS_COOKIE_EXPIRE);

    return "redirect:/";
  }
}
