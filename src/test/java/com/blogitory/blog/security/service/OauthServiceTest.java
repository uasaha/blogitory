package com.blogitory.blog.security.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.blogitory.blog.jwt.service.JwtService;
import com.blogitory.blog.member.dto.request.SignupOauthMemberRequestDto;
import com.blogitory.blog.member.entity.Member;
import com.blogitory.blog.member.entity.MemberDummy;
import com.blogitory.blog.member.repository.MemberRepository;
import com.blogitory.blog.role.entity.Role;
import com.blogitory.blog.role.entity.RoleDummy;
import com.blogitory.blog.role.repository.RoleRepository;
import com.blogitory.blog.rolemember.entity.RoleMember;
import com.blogitory.blog.rolemember.repository.RoleMemberRepository;
import com.blogitory.blog.security.adaptor.OauthAdaptor;
import com.blogitory.blog.security.dto.GithubAccessTokenResponseDto;
import com.blogitory.blog.security.dto.GithubUserInfoResponseDto;
import com.blogitory.blog.security.exception.AuthenticationException;
import com.blogitory.blog.security.exception.NotFoundOauthUser;
import com.blogitory.blog.security.service.impl.OauthServiceImpl;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

/**
 * 설명 작성 필!
 *
 * @author woonseok
 * @Date 2024-08-23
 * @since 1.0
 **/
class OauthServiceTest {
  OauthService oauthService;
  OauthAdaptor oauthAdaptor;
  MemberRepository memberRepository;
  RoleRepository roleRepository;
  JwtService jwtService;
  RoleMemberRepository roleMemberRepository;

  @BeforeEach
  void setUp() {
    oauthAdaptor = mock(OauthAdaptor.class);
    memberRepository = mock(MemberRepository.class);
    roleRepository = mock(RoleRepository.class);
    jwtService = mock(JwtService.class);
    roleMemberRepository = mock(RoleMemberRepository.class);

    oauthService = new OauthServiceImpl(oauthAdaptor, memberRepository,
            roleRepository, jwtService, roleMemberRepository);
  }

  @Test
  @DisplayName("소셜 회원가입")
  void oauthSignup() {
    SignupOauthMemberRequestDto requestDto = new SignupOauthMemberRequestDto(
            "provider", "id", "profile", "username", "name");

    when(memberRepository.save(any())).thenReturn(mock(Member.class));
    when(roleRepository.findById(anyInt())).thenReturn(Optional.of(mock(Role.class)));
    when(roleMemberRepository.save(any())).thenReturn(mock(RoleMember.class));

    oauthService.oauthSignup(requestDto);

    verify(memberRepository, times(1)).save(any(Member.class));
    verify(roleMemberRepository, times(1)).save(any(RoleMember.class));
  }

  @Test
  @DisplayName("깃허브 access token 발급")
  void getGithubAccessToken() {
    GithubAccessTokenResponseDto responseDto = new GithubAccessTokenResponseDto();
    ReflectionTestUtils.setField(responseDto, "accessToken", "access_token");
    ReflectionTestUtils.setField(responseDto, "tokenType", "tokenType");
    ReflectionTestUtils.setField(responseDto, "scope", "scope");

    when(oauthAdaptor.getGithubAccessToken(anyString())).thenReturn(responseDto);

    String result = oauthService.getGithubAccessToken("code");

    assertEquals(responseDto.getAccessToken(), result);
  }

  @Test
  @DisplayName("깃허브 access token 발급 실패")
  void getGithubAccessTokenFailed() {
    GithubAccessTokenResponseDto responseDto = new GithubAccessTokenResponseDto();

    when(oauthAdaptor.getGithubAccessToken(anyString())).thenReturn(responseDto);

    assertThrows(AuthenticationException.class, () -> oauthService.getGithubAccessToken("code"));
  }

  @Test
  @DisplayName("깃허브 로그인 성공")
  void githubLogin() {
    GithubUserInfoResponseDto responseDto = new GithubUserInfoResponseDto();
    ReflectionTestUtils.setField(responseDto, "login", "login");
    ReflectionTestUtils.setField(responseDto, "id", "id");
    ReflectionTestUtils.setField(responseDto, "avatarUrl", "avatarUrl");
    ReflectionTestUtils.setField(responseDto, "name", "name");

    Member member = MemberDummy.dummy();
    ReflectionTestUtils.setField(member, "oauth", "github");
    Role role = RoleDummy.dummy();
    String accessToken = "accessToken";

    when(oauthAdaptor.getGithubUserInfo(anyString())).thenReturn(responseDto);
    when(memberRepository.findByOauthId(anyString(), anyString())).thenReturn(Optional.of(member));
    when(roleRepository.findRolesByMemberNo(anyInt())).thenReturn(List.of(role.getRoleName()));
    when(jwtService.issue(anyString(), any())).thenReturn(accessToken);

    String result = oauthService.githubLogin(accessToken);

    assertEquals(accessToken, result);
  }

  @Test
  @DisplayName("깃허브 로그인 실패")
  void githubLoginFailed() {
    GithubUserInfoResponseDto responseDto = new GithubUserInfoResponseDto();
    ReflectionTestUtils.setField(responseDto, "login", "login");
    ReflectionTestUtils.setField(responseDto, "id", "id");
    ReflectionTestUtils.setField(responseDto, "avatarUrl", "avatarUrl");
    ReflectionTestUtils.setField(responseDto, "name", "name");

    Member member = MemberDummy.dummy();
    ReflectionTestUtils.setField(member, "oauth", "oauth");
    Role role = RoleDummy.dummy();
    String accessToken = "accessToken";

    when(oauthAdaptor.getGithubUserInfo(anyString())).thenReturn(responseDto);
    when(memberRepository.findByOauthId(anyString(), anyString())).thenReturn(Optional.of(member));
    when(roleRepository.findRolesByMemberNo(anyInt())).thenReturn(List.of(role.getRoleName()));
    when(jwtService.issue(anyString(), any())).thenReturn(accessToken);

    assertThrows(NotFoundOauthUser.class, () -> oauthService.githubLogin(accessToken));
  }
}