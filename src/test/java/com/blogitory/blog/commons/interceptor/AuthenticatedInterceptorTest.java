package com.blogitory.blog.commons.interceptor;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.blogitory.blog.blog.service.BlogService;
import com.blogitory.blog.member.service.MemberService;
import com.blogitory.blog.notice.service.NoticeService;
import com.blogitory.blog.security.users.UserDetailsImpl;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
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
  BlogService blogService;
  NoticeService noticeService;

  /**
   * Sets .
   */
  @BeforeEach
  void setup() {
    memberService = mock(MemberService.class);
    blogService = mock(BlogService.class);
    noticeService = mock(NoticeService.class);

    authenticatedInterceptor = new AuthenticatedInterceptor(blogService, noticeService);
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
            "pfp",
            roles);

    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails.getUserNo(), userDetails.getUsername(), roles);
    authentication.setDetails(userDetails);

    securityContext.setAuthentication(authentication);

    String thumbnail = "thumbs";

    when(memberService.getThumbnailByNo(any())).thenReturn(thumbnail);

    authenticatedInterceptor.postHandle(request, response, new Object(), mav);

    verify(mav, times(3)).addObject(any(), any());
  }
}