package com.blogitory.blog.security.handler;

import com.blogitory.blog.commons.utils.UrlUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

/**
 * Implementation of AuthenticationSuccessHandler.
 *
 * @author woonseok
 * @since 1.0
 **/
@Slf4j
public class AuthenticationSuccessHandlerImpl implements AuthenticationSuccessHandler {
  private static final String FAILED_PARAM = "login-failed";

  @Override
  public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                      Authentication authentication) throws IOException {
    String refererUrl = UrlUtil.getRefererUrl(request);

    if (refererUrl.contains(FAILED_PARAM)) {
      refererUrl = refererUrl.replace(FAILED_PARAM, "");
    }

    response.sendRedirect(refererUrl);
  }
}
