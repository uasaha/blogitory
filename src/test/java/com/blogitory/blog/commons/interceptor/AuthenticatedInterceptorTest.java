package com.blogitory.blog.commons.interceptor;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.blogitory.blog.member.dto.MemberPersistInfoDto;
import com.blogitory.blog.member.dto.MemberPersistInfoDtoDummy;
import com.blogitory.blog.member.service.MemberService;
import com.blogitory.blog.security.users.UserDetailsImpl;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.servlet.ModelAndView;

/**
 * 설명 작성 필!
 *
 * @author woonseok
 * @since 1.0
 */
class AuthenticatedInterceptorTest {
  /**
   * The Member service.
   */
  MemberService memberService;
  /**
   * The Authenticated interceptor.
   */
  AuthenticatedInterceptor authenticatedInterceptor;

  /**
   * Sets .
   */
  @BeforeEach
  void setup() {
    memberService = mock(MemberService.class);
    authenticatedInterceptor = new AuthenticatedInterceptor(memberService);
  }

  /**
   * Post handle.
   */
  @Test
  @DisplayName("인터셉터 unit test")
  void postHandle() {
    HttpServletRequest request = new MockHttpServletRequest();
    HttpServletResponse response = new MockHttpServletResponse();
    ModelAndView mav = mock(ModelAndView.class);
    SecurityContext securityContext = SecurityContextHolder.getContext();

    List<GrantedAuthority> roles = List.of(new SimpleGrantedAuthority("ROLE_DUMMY"));

    UserDetailsImpl userDetails = new UserDetailsImpl(
            "email",
            "password",
            1,
            "nickname",
            "name",
            roles);

    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails.getUserNo(), userDetails.getUsername(), roles);
    authentication.setDetails(userDetails);

    securityContext.setAuthentication(authentication);

    String thumbnail = "thumbs";

    when(memberService.getThumbnailByNo(any())).thenReturn(thumbnail);

    authenticatedInterceptor.postHandle(request, response, new Object(), mav);

    verify(mav, times(1)).addObject(any(), any());
  }
}