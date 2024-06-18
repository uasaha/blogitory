package com.blogitory.blog.commons.interceptor;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
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
import org.mockito.internal.handler.MockHandlerImpl;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
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
   *
   * @throws Exception the exception
   */
  @Test
  @DisplayName("인터셉터 unit test")
  void postHandle() throws Exception {
//    HttpServletRequest request = new MockHttpServletRequest();
//    HttpServletResponse response = new MockHttpServletResponse();
//    ModelAndView mav = mock(ModelAndView.class);
//    SecurityContext securityContext = mock(SecurityContext.class);
//
//    List<GrantedAuthority> roles = List.of(new SimpleGrantedAuthority("ROLE_DUMMY"));
//
//    UserDetails userDetails = new UserDetailsImpl(
//            "email",
//            "password",
//            1,
//            "nickname",
//            roles);
//
//    Authentication authentication = new UsernamePasswordAuthenticationToken("principal", userDetails, roles);
//    MemberPersistInfoDto infoDto = MemberPersistInfoDtoDummy.dummy();
//
//    when(securityContext.getAuthentication()).thenReturn(authentication);
////    when(authentication.isAuthenticated()).thenReturn(true);
////    when(authentication.getDetails()).thenReturn(userDetails);
//    SecurityContextHolder.setContext(securityContext);
//    when(memberService.persistInfo(anyInt())).thenReturn(infoDto);
//
//    authenticatedInterceptor.postHandle(request, response, new Object(), mav);
//
////    verify(memberService, times(1)).persistInfo(anyInt());
//
////    Assertions.assertEquals(mav.getModel().get("members"));
//    Map<String, Object> map = mav.getModel();
//    MemberPersistInfoDto actual = (MemberPersistInfoDto) map.get("members");
//    boolean thumb = (boolean) map.get("thumbIsNull");
//
//    Assertions.assertTrue(thumb);
//    Assertions.assertEquals(actual.getName(), infoDto.getName());
//    Assertions.assertEquals(actual.getThumb(), infoDto.getThumb());
  }
}