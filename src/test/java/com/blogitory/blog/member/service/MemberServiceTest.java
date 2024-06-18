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
 */
class MemberServiceTest {
  private MemberRepository memberRepository;
  private RoleRepository roleRepository;
  private RoleMemberRepository roleMemberRepository;
  private PasswordEncoder passwordEncoder;
  private MemberService memberService;
  private JwtService jwtService;

  /**
   * Sets up.
   */
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

  /**
   * Signup.
   */
  @Test
  @DisplayName("회원가입 - 성공")
  void signup() {
    Member member = MemberDummy.dummy();
    Role role = new Role(4, "ROLE_DUMMY");
    RoleMember roleMember = RoleMemberDummy.dummy(role, member);
    MemberSignupRequestDto requestDto = new MemberSignupRequestDto(
            member.getUsername(), member.getName(), member.getEmail(), member.getPassword());

    when(memberRepository.save(any())).thenReturn(member);
    when(roleRepository.findById(4)).thenReturn(Optional.of(role));
    when(roleMemberRepository.save(any())).thenReturn(roleMember);
    when(passwordEncoder.encode(member.getPassword())).thenReturn(member.getPassword());

    memberService.signup(requestDto);

    verify(memberRepository, times(1)).save(any());
    verify(roleRepository, times(1)).findById(role.getRoleNo());
    verify(roleMemberRepository, times(1)).save(any());
  }

  /**
   * Signup fail no roles.
   */
  @Test
  @DisplayName("회원가입 - 실패(권한 없음)")
  void signupFailNoRoles() {
    Member member = MemberDummy.dummy();
    Role role = new Role(4, "ROLE_DUMMY");
    RoleMember roleMember = RoleMemberDummy.dummy(role, member);
    MemberSignupRequestDto requestDto = new MemberSignupRequestDto(
            member.getUsername(), member.getName(), member.getEmail(), member.getPassword());

    when(memberRepository.save(any())).thenReturn(member);
    when(roleRepository.findById(4)).thenThrow(NotFoundException.class);
    when(roleMemberRepository.save(any())).thenReturn(roleMember);
    when(passwordEncoder.encode(member.getPassword())).thenReturn(member.getPassword());

    assertThatThrownBy(() -> memberService.signup(requestDto))
            .isInstanceOf(NotFoundException.class);
  }

  /**
   * Signup fail exist email.
   */
  @Test
  @DisplayName("회원가입 - 실패(이메일 중복)")
  void signupFailExistEmail() {
    Member member = MemberDummy.dummy();
    MemberSignupRequestDto requestDto = new MemberSignupRequestDto(
            member.getUsername(), member.getName(), member.getEmail(), member.getPassword());

    when(memberRepository.existsMemberByEmail(any())).thenReturn(true);

    assertThrows(MemberEmailAlreadyUsedException.class, () -> memberService.signup(requestDto));
  }

  @Test
  @DisplayName("회원가입 - 실패(유저네임 중복)")
  void signupFailExistUsername() {
    Member member = MemberDummy.dummy();
    MemberSignupRequestDto requestDto = new MemberSignupRequestDto(
            member.getUsername(), member.getName(), member.getEmail(), member.getPassword());

    when(memberRepository.existsMemberByUsername(any())).thenReturn(true);

    assertThrows(MemberEmailAlreadyUsedException.class, () -> memberService.signup(requestDto));
  }


  /**
   * Exist member by email.
   */
  @Test
  @DisplayName("이메일 중복")
  void existMemberByEmail() {
    String existEmail = MemberDummy.dummy().getEmail();

    when(memberRepository.existsMemberByEmail(existEmail)).thenReturn(true);

    boolean existExpect = memberService.existMemberByEmail(existEmail);

    verify(memberRepository, times(1)).existsMemberByEmail(any());
    assertTrue(existExpect);


  }

  /**
   * Not exist member by email.
   */
  @Test
  @DisplayName("이메일 중복 아님")
  void notExistMemberByEmail() {
    String notExistEmail = "notExist@not.com";

    when(memberRepository.existsMemberByEmail(notExistEmail)).thenReturn(false);

    boolean notExistExpect = memberService.existMemberByEmail(notExistEmail);

    verify(memberRepository, times(1)).existsMemberByEmail(any());
    assertFalse(notExistExpect);
  }

  /**
   * Login.
   */
  @Test
  @DisplayName("로그인 성공")
  void login() {
    MemberLoginRequestDto testDto = new MemberLoginRequestDto();
    ReflectionTestUtils.setField(testDto, "email", "test@email.com");
    ReflectionTestUtils.setField(testDto, "password", "123456");
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

  /**
   * Login failed not user.
   */
  @Test
  @DisplayName("로그인 실패 - 가입안된 이메일")
  void loginFailedNotUser() {
    MemberLoginRequestDto requestDto = new MemberLoginRequestDto("test@email.com", "password");

    when(memberRepository.findByEmail(any())).thenReturn(Optional.empty());

    assertThrows(NotFoundException.class, () -> memberService.login(requestDto));
  }

  /**
   * Login failed not matches password.
   */
  @Test
  @DisplayName("로그인 성공 - 비밀번호 틀림")
  void loginFailedNotMatchesPassword() {
    MemberLoginRequestDto requestDto = new MemberLoginRequestDto("test@email.com", "password");
    Member member = MemberDummy.dummy();

    when(memberRepository.findByEmail(any())).thenReturn(Optional.of(member));
    when(passwordEncoder.matches(any(), any())).thenReturn(false);

    assertThrows(AuthenticationException.class, () -> memberService.login(requestDto));
  }

  /**
   * My profile.
   */
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

  /**
   * My profile failed.
   */
  @Test
  @DisplayName("프로필 조회 실패")
  void myProfileFailed() {
    when(memberRepository.getMyProfile(any())).thenReturn(Optional.empty());

    assertThrows(NotFoundException.class, () -> memberService.myProfile(0));
  }

  /**
   * Persist info.
   */
  @Test
  @DisplayName("로그인정보 조회")
  void persistInfo() {
    MemberPersistInfoDto infoDto = new MemberPersistInfoDto("username", "name", "thumb");

    when(memberRepository.getPersistInfo(any())).thenReturn(Optional.of(infoDto));

    MemberPersistInfoDto actual = memberService.persistInfo(0);

    assertAll(
            () -> assertEquals(infoDto.getUsername(), actual.getUsername()),
            () -> assertEquals(infoDto.getName(), actual.getName()),
            () -> assertEquals(infoDto.getThumb(), actual.getThumb())
    );
  }

  /**
   * Persist info failed.
   */
  @Test
  @DisplayName("로그인정보 조회 실패")
  void persistInfofailed() {
    when(memberRepository.getPersistInfo(any())).thenReturn(Optional.empty());

    assertThrows(NotFoundException.class, () -> memberService.persistInfo(0));
  }

  /**
   * Update name.
   */
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

  /**
   * Update name failed.
   */
  @Test
  @DisplayName("이름 수정 실패")
  void updateNameFailed() {
    MemberUpdateNameRequestDto requestDto = new MemberUpdateNameRequestDto();
    ReflectionTestUtils.setField(requestDto, "name", "updatedName");

    when(memberRepository.findById(any())).thenReturn(Optional.empty());


    assertThrows(NotFoundException.class, () -> memberService.updateName(0, requestDto));
  }

  /**
   * Update open email.
   */
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

  /**
   * Update open email failed.
   */
  @Test
  @DisplayName("공개이메일 수정 실패")
  void updateOpenEmailFailed() {
    MemberUpdateProfileRequestDto requestDto = new MemberUpdateProfileRequestDto();
    ReflectionTestUtils.setField(requestDto, "content", "updated");

    when(memberRepository.findById(any())).thenReturn(Optional.empty());

    assertThrows(NotFoundException.class, () -> memberService.updateOpenEmail(0, requestDto));
  }

  /**
   * Update GitHub.
   */
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

  /**
   * Update GitHub failed.
   */
  @Test
  @DisplayName("깃허브 수정 실패")
  void updateGithubFailed() {
    MemberUpdateProfileRequestDto requestDto = new MemberUpdateProfileRequestDto();
    ReflectionTestUtils.setField(requestDto, "content", "updated");

    when(memberRepository.findById(any())).thenReturn(Optional.empty());

    assertThrows(NotFoundException.class, () -> memberService.updateGithub(0, requestDto));
  }

  /**
   * Update facebook.
   */
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

  /**
   * Update facebook failed.
   */
  @Test
  @DisplayName("페이스북 수정 실패")
  void updateFacebookFailed() {
    MemberUpdateProfileRequestDto requestDto = new MemberUpdateProfileRequestDto();
    ReflectionTestUtils.setField(requestDto, "content", "updated");

    when(memberRepository.findById(any())).thenReturn(Optional.empty());

    assertThrows(NotFoundException.class, () -> memberService.updateFacebook(0, requestDto));
  }

  /**
   * Update x.
   */
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

  /**
   * Update x failed.
   */
  @Test
  @DisplayName("X 수정 실패")
  void updateXFailed() {
    MemberUpdateProfileRequestDto requestDto = new MemberUpdateProfileRequestDto();
    ReflectionTestUtils.setField(requestDto, "content", "updated");

    when(memberRepository.findById(any())).thenReturn(Optional.empty());

    assertThrows(NotFoundException.class, () -> memberService.updateX(0, requestDto));
  }

  /**
   * Update homepage.
   */
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

  /**
   * Update homepage failed.
   */
  @Test
  @DisplayName("홈페이지 수정 실패")
  void updateHomepageFailed() {
    MemberUpdateProfileRequestDto requestDto = new MemberUpdateProfileRequestDto();
    ReflectionTestUtils.setField(requestDto, "content", "updated");

    when(memberRepository.findById(any())).thenReturn(Optional.empty());

    assertThrows(NotFoundException.class, () -> memberService.updateHomepage(0, requestDto));
  }

  /**
   * Is duplicated name.
   */
  @Test
  @DisplayName("회원 이름 중복 검사")
  void isDuplicatedName() {
    String name = "name";

    when(memberRepository.existsMemberByUsername(name)).thenReturn(true);

    assertTrue(memberService.isDuplicateUsername(name));
  }

  /**
   * Is duplicated name false.
   */
  @Test
  @DisplayName("회원 유저네임 중복 검사")
  void isDuplicatedNameFalse() {
    String name = "name";

    when(memberRepository.existsMemberByUsername(name)).thenReturn(false);

    assertFalse(memberService.isDuplicateUsername(name));
  }

  @Test
  @DisplayName("회원 한줄소개 수정 성공")
  void updateBio() {
    MemberUpdateProfileRequestDto requestDto = new MemberUpdateProfileRequestDto();
    ReflectionTestUtils.setField(requestDto, "content", "bio_test");
    Member member = MemberDummy.dummy();

    when(memberRepository.findById(member.getMemberNo()))
            .thenReturn(Optional.of(member));

    memberService.updateBio(member.getMemberNo(), requestDto);

    assertEquals(member.getBio(), requestDto.getContent());
  }

  @Test
  @DisplayName("회원 한줄소개 수정 실패")
  void updateBioFailed() {
    MemberUpdateProfileRequestDto requestDto = new MemberUpdateProfileRequestDto();
    ReflectionTestUtils.setField(requestDto, "content", "bio_test");

    when(memberRepository.findById(any())).thenReturn(Optional.empty());

    assertThrows(NotFoundException.class, () -> memberService.updateBio(0, requestDto));
  }

  @Test
  @DisplayName("회원번호로 썸네일 조회 성공")
  void getThumbnailByNo() {
    Member member = MemberDummy.dummy();

    when(memberRepository.findProfileThumbByMemberNo(member.getMemberNo()))
            .thenReturn(member.getProfileThumb());

    String thumbnail = memberService.getThumbnailByNo(member.getMemberNo());

    assertEquals(thumbnail, member.getProfileThumb());
  }

  @Test
  @DisplayName("이메일로 비밀번호 조회 성공")
  void getPasswordByEmail() {
    Member member = MemberDummy.dummy();

    when(memberRepository.findByEmail(member.getEmail()))
            .thenReturn(Optional.of(member));

    String password = memberService.getPasswordByEmail(member.getEmail());

    assertEquals(password, member.getPassword());
  }

  @Test
  @DisplayName("이메일로 비밀번호 조회 실패")
  void getPasswordByEmailFailed() {
    String email = "email@email.com";

    when(memberRepository.findByEmail(email))
            .thenReturn(Optional.empty());

    assertThrows(NotFoundException.class, () -> memberService.getPasswordByEmail(email));
  }
}