package com.blogitory.blog.member.service;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.blogitory.blog.commons.exception.NotFoundException;
import com.blogitory.blog.jwt.service.JwtService;
import com.blogitory.blog.member.dto.MemberLoginRequestDto;
import com.blogitory.blog.member.dto.MemberMyProfileResponseDto;
import com.blogitory.blog.member.dto.MemberPersistInfoDto;
import com.blogitory.blog.member.dto.MemberSignupRequestDto;
import com.blogitory.blog.member.dto.MemberUpdateNameRequestDto;
import com.blogitory.blog.member.dto.MemberUpdateProfileRequestDto;
import com.blogitory.blog.member.entity.Member;
import com.blogitory.blog.member.entity.MemberDummy;
import com.blogitory.blog.member.exception.MemberEmailAlreadyUsedException;
import com.blogitory.blog.member.repository.MemberRepository;
import com.blogitory.blog.member.service.impl.MemberServiceImpl;
import com.blogitory.blog.role.entity.Role;
import com.blogitory.blog.role.repository.RoleRepository;
import com.blogitory.blog.rolemember.entity.RoleMember;
import com.blogitory.blog.rolemember.entity.RoleMemberDummy;
import com.blogitory.blog.rolemember.repository.RoleMemberRepository;
import com.blogitory.blog.security.exception.AuthenticationException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;

/**
 * MemberService test.
 *
 * @author woonseok
 * @since 1.0
 **/
class MemberServiceTest {
  private MemberRepository memberRepository;
  private RoleRepository roleRepository;
  private RoleMemberRepository roleMemberRepository;
  private PasswordEncoder passwordEncoder;
  private MemberService memberService;
  private JwtService jwtService;

  @BeforeEach
  void setUp() {
    memberRepository = mock(MemberRepository.class);
    roleRepository = mock(RoleRepository.class);
    roleMemberRepository = mock(RoleMemberRepository.class);
    passwordEncoder = mock(PasswordEncoder.class);
    jwtService = mock(JwtService.class);

    memberService = new MemberServiceImpl(
            memberRepository, roleRepository, roleMemberRepository, jwtService, passwordEncoder);
  }

  @Test
  @DisplayName("회원가입 - 성공")
  void signup() {
    Member member = MemberDummy.dummy();
    Role role = new Role(4, "ROLE_DUMMY");
    RoleMember roleMember = RoleMemberDummy.dummy(role, member);
    MemberSignupRequestDto requestDto = new MemberSignupRequestDto(
            member.getName(), member.getEmail(), member.getPassword());

    when(memberRepository.save(any())).thenReturn(member);
    when(roleRepository.findById(4)).thenReturn(Optional.of(role));
    when(roleMemberRepository.save(any())).thenReturn(roleMember);
    when(passwordEncoder.encode(member.getPassword())).thenReturn(member.getPassword());

    memberService.signup(requestDto);

    verify(memberRepository, times(1)).save(any());
    verify(roleRepository, times(1)).findById(role.getRoleNo());
    verify(roleMemberRepository, times(1)).save(any());
  }

  @Test
  @DisplayName("회원가입 - 실패(권한 없음)")
  void signupFailNoRoles() {
    Member member = MemberDummy.dummy();
    Role role = new Role(4, "ROLE_DUMMY");
    RoleMember roleMember = RoleMemberDummy.dummy(role, member);
    MemberSignupRequestDto requestDto = new MemberSignupRequestDto(
            member.getName(), member.getEmail(), member.getPassword());

    when(memberRepository.save(any())).thenReturn(member);
    when(roleRepository.findById(4)).thenThrow(NotFoundException.class);
    when(roleMemberRepository.save(any())).thenReturn(roleMember);
    when(passwordEncoder.encode(member.getPassword())).thenReturn(member.getPassword());

    assertThatThrownBy(() -> memberService.signup(requestDto))
            .isInstanceOf(NotFoundException.class);
  }

  @Test
  @DisplayName("회원가입 - 실패(이메일 중복)")
  void signupFailExistEmail() {
    Member member = MemberDummy.dummy();
    MemberSignupRequestDto requestDto = new MemberSignupRequestDto(
            member.getName(), member.getEmail(), member.getPassword());

    when(memberRepository.existsMemberByEmail(any())).thenReturn(true);

    assertThrows(MemberEmailAlreadyUsedException.class, () -> memberService.signup(requestDto));
  }


  @Test
  @DisplayName("이메일 중복")
  void existMemberByEmail() {
    String existEmail = MemberDummy.dummy().getEmail();

    when(memberRepository.existsMemberByEmail(existEmail)).thenReturn(true);

    boolean existExpect = memberService.existMemberByEmail(existEmail);

    verify(memberRepository, times(1)).existsMemberByEmail(any());
    assertTrue(existExpect);


  }

  @Test
  @DisplayName("이메일 중복 아님")
  void notExistMemberByEmail() {
    String notExistEmail = "notExist@not.com";

    when(memberRepository.existsMemberByEmail(notExistEmail)).thenReturn(false);

    boolean notExistExpect = memberService.existMemberByEmail(notExistEmail);

    verify(memberRepository, times(1)).existsMemberByEmail(any());
    assertFalse(notExistExpect);
  }

  @Test
  @DisplayName("로그인 성공")
  void login() {
    MemberLoginRequestDto requestDto = new MemberLoginRequestDto("test@email.com", "password");
    Member member = MemberDummy.dummy();
    List<String> roles = List.of("ROLE_DUMMY");

    String expect = "accessToken";

    when(memberRepository.findByEmail(any())).thenReturn(Optional.of(member));
    when(passwordEncoder.matches(any(), any())).thenReturn(true);
    when(roleRepository.findRolesByMemberNo(any())).thenReturn(roles);
    when(jwtService.issue(any(), any(), any())).thenReturn(expect);

    String actual = memberService.login(requestDto);

    assertEquals(expect, actual);
  }

  @Test
  @DisplayName("로그인 실패 - 가입안된 이메일")
  void loginFailedNotUser() {
    MemberLoginRequestDto requestDto = new MemberLoginRequestDto("test@email.com", "password");

    when(memberRepository.findByEmail(any())).thenReturn(Optional.empty());

    assertThrows(AuthenticationException.class, () -> memberService.login(requestDto));
  }

  @Test
  @DisplayName("로그인 성공 - 비밀번호 틀림")
  void loginFailedNotMatchesPassword() {
    MemberLoginRequestDto requestDto = new MemberLoginRequestDto("test@email.com", "password");
    Member member = MemberDummy.dummy();

    when(memberRepository.findByEmail(any())).thenReturn(Optional.of(member));
    when(passwordEncoder.matches(any(), any())).thenReturn(false);

    assertThrows(AuthenticationException.class, () -> memberService.login(requestDto));
  }

  @Test
  @DisplayName("프로필 조회 성공")
  void myProfile() {
    MemberMyProfileResponseDto responseDto =
            new MemberMyProfileResponseDto("email", "name",
                    "profileThumb", "introEmail",
                    "github", "x",
                    "facebook", "homepage",
                    LocalDateTime.of(2024, 4, 25, 0, 0));

    when(memberRepository.getMyProfile(any())).thenReturn(Optional.of(responseDto));

    MemberMyProfileResponseDto actual = memberService.myProfile(0);

    assertAll(
            () -> assertEquals(responseDto.getEmail(), actual.getEmail()),
            () -> assertEquals(responseDto.getName(), actual.getName()),
            () -> assertEquals(responseDto.getProfileThumb(), actual.getProfileThumb()),
            () -> assertEquals(responseDto.getIntroEmail(), actual.getIntroEmail()),
            () -> assertEquals(responseDto.getGithub(), actual.getGithub()),
            () -> assertEquals(responseDto.getTwitter(), actual.getTwitter()),
            () -> assertEquals(responseDto.getFacebook(), actual.getFacebook()),
            () -> assertEquals(responseDto.getHomepage(), actual.getHomepage()),
            () -> assertEquals(responseDto.getCreatedAt(), actual.getCreatedAt())
    );
  }

  @Test
  @DisplayName("프로필 조회 실패")
  void myProfileFailed() {
    when(memberRepository.getMyProfile(any())).thenReturn(Optional.empty());

    assertThrows(NotFoundException.class, () -> memberService.myProfile(0));
  }

  @Test
  @DisplayName("로그인정보 조회")
  void persistInfo() {
    MemberPersistInfoDto infoDto = new MemberPersistInfoDto("name", "thumb");

    when(memberRepository.getPersistInfo(any())).thenReturn(Optional.of(infoDto));

    MemberPersistInfoDto actual = memberService.persistInfo(0);

    assertAll(
            () -> assertEquals(infoDto.getName(), actual.getName()),
            () -> assertEquals(infoDto.getThumb(), actual.getThumb())
    );
  }

  @Test
  @DisplayName("로그인정보 조회 실패")
  void persistInfofailed() {
    when(memberRepository.getPersistInfo(any())).thenReturn(Optional.empty());

    assertThrows(NotFoundException.class, () -> memberService.persistInfo(0));
  }

  @Test
  @DisplayName("이름 수정 성공")
  void updateName() {
    Member member = MemberDummy.dummy();
    MemberUpdateNameRequestDto requestDto = new MemberUpdateNameRequestDto();
    ReflectionTestUtils.setField(requestDto, "name", "updatedName");

    when(memberRepository.findById(any())).thenReturn(Optional.of(member));

    memberService.updateName(0, requestDto);

    assertEquals(member.getName(), requestDto.getName());
  }

  @Test
  @DisplayName("이름 수정 실패")
  void updateNameFailed() {
    MemberUpdateNameRequestDto requestDto = new MemberUpdateNameRequestDto();
    ReflectionTestUtils.setField(requestDto, "name", "updatedName");

    when(memberRepository.findById(any())).thenReturn(Optional.empty());


    assertThrows(NotFoundException.class, () -> memberService.updateName(0, requestDto));
  }

  @Test
  @DisplayName("공개이메일 수정 성공")
  void updateOpenEmail() {
    Member member = MemberDummy.dummy();
    MemberUpdateProfileRequestDto requestDto = new MemberUpdateProfileRequestDto();
    ReflectionTestUtils.setField(requestDto, "content", "updated");

    when(memberRepository.findById(any())).thenReturn(Optional.of(member));

    memberService.updateOpenEmail(0, requestDto);

    assertEquals(member.getIntroEmail(), requestDto.getContent());
  }

  @Test
  @DisplayName("공개이메일 수정 실패")
  void updateOpenEmailFailed() {
    MemberUpdateProfileRequestDto requestDto = new MemberUpdateProfileRequestDto();
    ReflectionTestUtils.setField(requestDto, "content", "updated");

    when(memberRepository.findById(any())).thenReturn(Optional.empty());

    assertThrows(NotFoundException.class, () -> memberService.updateOpenEmail(0, requestDto));
  }

  @Test
  @DisplayName("깃허브 수정 성공")
  void updateGithub() {
    Member member = MemberDummy.dummy();
    MemberUpdateProfileRequestDto requestDto = new MemberUpdateProfileRequestDto();
    ReflectionTestUtils.setField(requestDto, "content", "updated");

    when(memberRepository.findById(any())).thenReturn(Optional.of(member));

    memberService.updateGithub(0, requestDto);

    assertEquals(member.getGithub(), requestDto.getContent());
  }

  @Test
  @DisplayName("깃허브 수정 실패")
  void updateGithubFailed() {
    MemberUpdateProfileRequestDto requestDto = new MemberUpdateProfileRequestDto();
    ReflectionTestUtils.setField(requestDto, "content", "updated");

    when(memberRepository.findById(any())).thenReturn(Optional.empty());

    assertThrows(NotFoundException.class, () -> memberService.updateGithub(0, requestDto));
  }

  @Test
  @DisplayName("페이스북 수정 성공")
  void updateFacebook() {
    Member member = MemberDummy.dummy();
    MemberUpdateProfileRequestDto requestDto = new MemberUpdateProfileRequestDto();
    ReflectionTestUtils.setField(requestDto, "content", "updated");

    when(memberRepository.findById(any())).thenReturn(Optional.of(member));

    memberService.updateFacebook(0, requestDto);

    assertEquals(member.getFacebook(), requestDto.getContent());
  }

  @Test
  @DisplayName("페이스북 수정 실패")
  void updateFacebookFailed() {
    MemberUpdateProfileRequestDto requestDto = new MemberUpdateProfileRequestDto();
    ReflectionTestUtils.setField(requestDto, "content", "updated");

    when(memberRepository.findById(any())).thenReturn(Optional.empty());

    assertThrows(NotFoundException.class, () -> memberService.updateFacebook(0, requestDto));
  }

  @Test
  @DisplayName("X 수정 성공")
  void updateX() {
    Member member = MemberDummy.dummy();
    MemberUpdateProfileRequestDto requestDto = new MemberUpdateProfileRequestDto();
    ReflectionTestUtils.setField(requestDto, "content", "updated");

    when(memberRepository.findById(any())).thenReturn(Optional.of(member));

    memberService.updateX(0, requestDto);

    assertEquals(member.getTwitter(), requestDto.getContent());
  }

  @Test
  @DisplayName("X 수정 실패")
  void updateXFailed() {
    MemberUpdateProfileRequestDto requestDto = new MemberUpdateProfileRequestDto();
    ReflectionTestUtils.setField(requestDto, "content", "updated");

    when(memberRepository.findById(any())).thenReturn(Optional.empty());

    assertThrows(NotFoundException.class, () -> memberService.updateX(0, requestDto));
  }

  @Test
  @DisplayName("홈페이지 수정 성공")
  void updateHomepage() {
    Member member = MemberDummy.dummy();
    MemberUpdateProfileRequestDto requestDto = new MemberUpdateProfileRequestDto();
    ReflectionTestUtils.setField(requestDto, "content", "updated");

    when(memberRepository.findById(any())).thenReturn(Optional.of(member));

    memberService.updateHomepage(0, requestDto);

    assertEquals(member.getHomepage(), requestDto.getContent());
  }

  @Test
  @DisplayName("홈페이지 수정 실패")
  void updateHomepageFailed() {
    MemberUpdateProfileRequestDto requestDto = new MemberUpdateProfileRequestDto();
    ReflectionTestUtils.setField(requestDto, "content", "updated");

    when(memberRepository.findById(any())).thenReturn(Optional.empty());

    assertThrows(NotFoundException.class, () -> memberService.updateHomepage(0, requestDto));
  }
}