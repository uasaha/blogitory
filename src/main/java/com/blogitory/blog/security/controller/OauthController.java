package com.blogitory.blog.security.controller;

import static com.blogitory.blog.security.util.JwtUtils.ACCESS_COOKIE_EXPIRE;
import static com.blogitory.blog.security.util.JwtUtils.ACCESS_TOKEN_COOKIE_NAME;

import com.blogitory.blog.commons.properties.GithubOauthProperties;
import com.blogitory.blog.commons.utils.CookieUtils;
import com.blogitory.blog.member.dto.request.SignupOauthMemberRequestDto;
import com.blogitory.blog.security.exception.NotFoundOauthUser;
import com.blogitory.blog.security.service.OauthService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
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
  private final GithubOauthProperties githubOauthProperties;
  private final CookieUtils cookieUtils;

  @GetMapping("/login/oauth/github/auth")
  public String goToGithub() {
    return "redirect:https://github.com/login/oauth/authorize?client_id=" + githubOauthProperties.getClientId();
  }

  /**
   * Login with GitHub Oauth.
   *
   * @param code     verification code
   * @param response HttpServletResponse
   * @return main page view
   */
  @GetMapping("/login/oauth/github")
  public String githubOauthLogin(@RequestParam String code,
                                 HttpServletResponse response) {
    String githubAccessToken = oauthService.getGithubAccessToken(code);

    String accessToken = oauthService.githubLogin(githubAccessToken);

    cookieUtils.addSecureCookie(
            response, ACCESS_TOKEN_COOKIE_NAME, accessToken, ACCESS_COOKIE_EXPIRE);

    return "redirect:/";
  }

  /**
   * Signup user with oauth.
   *
   * @param requestDto request dto
   * @return 301
   */
  @PostMapping("/signup/oauth")
  public String signupOauth(@Valid SignupOauthMemberRequestDto requestDto) {

    oauthService.oauthSignup(requestDto);

    return "redirect:/";
  }

  /**
   * Not founded oauth user.
   *
   * @param notFoundOauthUser exception
   * @param model             model
   * @return signup page
   */
  @ExceptionHandler(NotFoundOauthUser.class)
  public String notFoundOauthUser(NotFoundOauthUser notFoundOauthUser,
                                  Model model) {

    model.addAttribute("user", notFoundOauthUser);

    return "index/main/oauth-signup";
  }
}
