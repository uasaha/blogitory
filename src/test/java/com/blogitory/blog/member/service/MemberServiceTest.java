package com.blogitory.blog.member.service;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.blogitory.blog.blog.entity.Blog;
import com.blogitory.blog.blog.entity.BlogDummy;
import com.blogitory.blog.blog.repository.BlogRepository;
import com.blogitory.blog.commons.exception.NotFoundException;
import com.blogitory.blog.follow.repository.FollowRepository;
import com.blogitory.blog.jwt.service.JwtService;
import com.blogitory.blog.link.entity.Link;
import com.blogitory.blog.link.repository.LinkRepository;
import com.blogitory.blog.member.dto.request.MemberLoginRequestDto;
import com.blogitory.blog.member.dto.request.UpdatePasswordRequestDto;
import com.blogitory.blog.member.dto.response.MemberPersistInfoDto;
import com.blogitory.blog.member.dto.response.MemberProfileResponseDto;
import com.blogitory.blog.member.dto.request.MemberSignupRequestDto;
import com.blogitory.blog.member.dto.request.MemberUpdateProfileRequestDto;
import com.blogitory.blog.member.dto.response.MemberSettingsAlertResponseDto;
import com.blogitory.blog.member.dto.response.MemberSettingsProfileResponseDto;
import com.blogitory.blog.member.entity.Member;
import com.blogitory.blog.member.entity.MemberDummy;
import com.blogitory.blog.member.exception.MemberEmailAlreadyUsedException;
import com.blogitory.blog.member.exception.MemberPwdChangeFailedException;
import com.blogitory.blog.member.repository.MemberRepository;
import com.blogitory.blog.member.service.impl.MemberServiceImpl;
import com.blogitory.blog.role.entity.Role;
import com.blogitory.blog.role.repository.RoleRepository;
import com.blogitory.blog.rolemember.entity.RoleMember;
import com.blogitory.blog.rolemember.entity.RoleMemberDummy;
import com.blogitory.blog.rolemember.repository.RoleMemberRepository;
import com.blogitory.blog.security.exception.AuthenticationException;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
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
  private FollowRepository followRepository;
  private LinkRepository linkRepository;
  private BlogRepository blogRepository;
  private RedisTemplate<String, Object> redisTemplate;

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
    followRepository = mock(FollowRepository.class);
    linkRepository = mock(LinkRepository.class);
    redisTemplate = mock(RedisTemplate.class);
    blogRepository = mock(BlogRepository.class);

    memberService = new MemberServiceImpl(
            memberRepository, roleRepository, roleMemberRepository, followRepository, linkRepository, blogRepository, jwtService, passwordEncoder, redisTemplate);
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
    when(jwtService.issue(any(), any())).thenReturn(expect);

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
  void persistInfoFailed() {
    when(memberRepository.getPersistInfo(any())).thenReturn(Optional.empty());

    assertThrows(NotFoundException.class, () -> memberService.persistInfo(0));
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

  @Test
  @DisplayName("프로필 조회 성공")
  void getProfileByUsername() {
    Member member = MemberDummy.dummy();
    Long follower = 1L;
    Long followee = 1L;

    ReflectionTestUtils.setField(member, "links", List.of(new Link(1L, member, "link")));
    ReflectionTestUtils.setField(member, "blogs", List.of(BlogDummy.dummy(member)));

    when(memberRepository.findByUsername(any())).thenReturn(Optional.of(member));
    when(followRepository.countFollower(anyInt())).thenReturn(follower);
    when(followRepository.countFollowee(anyInt())).thenReturn(followee);

    MemberProfileResponseDto responseDto = memberService.getProfileByUsername(member.getUsername());

    assertEquals(responseDto.getUsername(), member.getUsername());
    assertEquals(responseDto.getName(), member.getName());
    assertEquals(responseDto.getBio(), member.getBio());
    assertEquals(responseDto.getProfileThumb(), member.getProfileThumb());
    assertEquals(responseDto.getIntroEmail(), member.getIntroEmail());
    assertEquals(responseDto.getFolloweeCnt(), followee);
    assertEquals(responseDto.getFollowerCnt(), follower);
    assertEquals(1, responseDto.getBlogs().size());
    assertEquals(1, responseDto.getLinks().size());
  }

  @Test
  @DisplayName("프로필 조회 실패")
  void getProfileByUsernameFailed() {
    Member member = MemberDummy.dummy();
    ReflectionTestUtils.setField(member, "links", List.of());
    ReflectionTestUtils.setField(member, "blogs", List.of());

    when(memberRepository.findByUsername(any())).thenReturn(Optional.empty());

    assertThrows(NotFoundException.class, () -> memberService.getProfileByUsername("username"));
  }

  @Test
  @DisplayName("프로필 조회 실패 - 탈퇴")
  void getProfileByUsernameFailedLeft() {
    Member member = MemberDummy.dummy();
    ReflectionTestUtils.setField(member, "left", true);
    ReflectionTestUtils.setField(member, "blogs", List.of());

    when(memberRepository.findByUsername(any())).thenReturn(Optional.of(member));

    assertThrows(NotFoundException.class, () -> memberService.getProfileByUsername("username"));
  }

  @Test
  @DisplayName("프로필 업데이트 성공")
  void updateProfile() {
    Member member = MemberDummy.dummy();

    ReflectionTestUtils.setField(member, "links", List.of(new Link(1L, member, "link")));
    ReflectionTestUtils.setField(member, "blogs", List.of());

    MemberUpdateProfileRequestDto requestDto = new MemberUpdateProfileRequestDto();
    ReflectionTestUtils.setField(requestDto, "name", "newName");
    ReflectionTestUtils.setField(requestDto, "bio", "newBio");
    ReflectionTestUtils.setField(requestDto, "email", "newEmail@email.com");
    ReflectionTestUtils.setField(requestDto, "linkList", List.of("new links"));

    Link link = new Link(2L, member, "new links");

    when(memberRepository.findById(any())).thenReturn(Optional.of(member));
    doNothing().when(linkRepository).deleteById(any());
    when(linkRepository.save(any())).thenReturn(link);

    memberService.updateProfile(member.getMemberNo(), requestDto);

    verify(linkRepository, times(1)).deleteById(anyLong());
    verify(linkRepository, times(1)).save(any(Link.class));
  }

  @Test
  @DisplayName("프로필 업데이트 실패")
  void updateProfileFailed() {
    Member member = MemberDummy.dummy();

    ReflectionTestUtils.setField(member, "links", List.of(new Link(1L, member, "link")));
    ReflectionTestUtils.setField(member, "blogs", List.of());

    MemberUpdateProfileRequestDto requestDto = new MemberUpdateProfileRequestDto();
    ReflectionTestUtils.setField(requestDto, "name", "newName");
    ReflectionTestUtils.setField(requestDto, "bio", "newBio");
    ReflectionTestUtils.setField(requestDto, "email", "newEmail@email.com");
    ReflectionTestUtils.setField(requestDto, "linkList", List.of("new links"));

    when(memberRepository.findById(1)).thenReturn(Optional.empty());

    assertThrows(NotFoundException.class,
            () -> memberService.updateProfile(1, requestDto));
  }

  @Test
  @DisplayName("설정 프로필 조회")
  void getSettingsProfile() {
    Member member = MemberDummy.dummy();
    Link link = new Link(1L, member, "link");
    ReflectionTestUtils.setField(member, "links", List.of(link));

    when(memberRepository.findById(any())).thenReturn(Optional.of(member));

    MemberSettingsProfileResponseDto responseDto = memberService.getSettingsProfile(member.getMemberNo());

    assertEquals(responseDto.getUsername(), member.getUsername());
    assertEquals(responseDto.getName(), member.getName());
    assertEquals(responseDto.getBio(), member.getBio());
    assertEquals(responseDto.getEmail(), member.getEmail());
    assertEquals(responseDto.getIntroEmail(), member.getIntroEmail());
    assertEquals(responseDto.getPfpUrl(), member.getProfileThumb());
    assertEquals(responseDto.getLinks().get(0), link.getUrl());
  }

  @Test
  @DisplayName("설정 프로필 조회 실패")
  void getSettingsProfileFailed() {
    when(memberRepository.findById(any())).thenReturn(Optional.empty());

    assertThrows(NotFoundException.class, () -> memberService.getSettingsProfile(1));
  }

  @Test
  @DisplayName("설정 알림 조회")
  void getSettingsAlert() {
    Member member = MemberDummy.dummy();
    when(memberRepository.findById(any())).thenReturn(Optional.of(member));

    MemberSettingsAlertResponseDto responseDto = memberService.getSettingsAlert(member.getMemberNo());

    assertEquals(responseDto.isCommentAlert(), member.isCommentAlert());
    assertEquals(responseDto.isFollowAlert(), member.isFollowAlert());
    assertEquals(responseDto.isHeartAlert(), member.isHeartAlert());
    assertEquals(responseDto.isNewPostsAlert(), member.isNewAlert());
  }

  @Test
  @DisplayName("설정 알림 조회 실패")
  void getSettingsAlertFailed() {
    when(memberRepository.findById(any())).thenReturn(Optional.empty());

    assertThrows(NotFoundException.class, () -> memberService.getSettingsAlert(1));
  }

  @Test
  @DisplayName("알림 설정")
  void updateAlerts() {
    Member member = MemberDummy.dummy();
    when(memberRepository.findById(any())).thenReturn(Optional.of(member));

    memberService.updateAlerts(1, "COMMENT", false);
    assertFalse(member.isCommentAlert());

    memberService.updateAlerts(1, "FOLLOW", false);
    assertFalse(member.isFollowAlert());

    memberService.updateAlerts(1, "HEART", false);
    assertFalse(member.isHeartAlert());

    memberService.updateAlerts(1, "NEW_POSTS", false);
    assertFalse(member.isNewAlert());
  }

  @Test
  @DisplayName("알림 설정 실패")
  void updateAlertsFailed() {
    when(memberRepository.findById(any())).thenReturn(Optional.empty());

    assertThrows(NotFoundException.class, () -> memberService.updateAlerts(1, "COMMENT", false));
  }

  @Test
  @DisplayName("비밀번호 수정")
  void updatePassword() {
    Member member = MemberDummy.dummy();
    String email = member.getEmail();
    when(memberRepository.findByEmail(any())).thenReturn(Optional.of(member));
    ValueOperations<String, Object> operations = mock(ValueOperations.class);
    when(redisTemplate.opsForValue()).thenReturn(operations);
    when(operations.getAndDelete(anyString())).thenReturn(email);

    UpdatePasswordRequestDto requestDto =
            new UpdatePasswordRequestDto("ui", "@Test12345");

    when(passwordEncoder.encode(anyString())).thenReturn(requestDto.getPwd());

    memberService.updatePassword(requestDto);

    assertEquals(requestDto.getPwd(), member.getPassword());
  }

  @Test
  @DisplayName("비밀번호 수정 실패 - 없는 회원")
  void updatePasswordFailed() {
    Member member = MemberDummy.dummy();
    String email = member.getEmail();
    when(memberRepository.findByEmail(any())).thenReturn(Optional.empty());
    ValueOperations<String, Object> operations = mock(ValueOperations.class);
    when(redisTemplate.opsForValue()).thenReturn(operations);
    when(operations.getAndDelete(anyString())).thenReturn(email);

    UpdatePasswordRequestDto requestDto =
            new UpdatePasswordRequestDto("ui", "@Test12345");

    when(passwordEncoder.encode(anyString())).thenReturn(requestDto.getPwd());

    assertThrows(MemberPwdChangeFailedException.class, () -> memberService.updatePassword(requestDto));
  }

  @Test
  @DisplayName("비밀번호 수정 실패 - 없는 메일")
  void updatePasswordFailedNoMail() {
    Member member = MemberDummy.dummy();
    when(memberRepository.findByEmail(any())).thenReturn(Optional.of(member));
    ValueOperations<String, Object> operations = mock(ValueOperations.class);
    when(redisTemplate.opsForValue()).thenReturn(operations);
    when(operations.getAndDelete(anyString())).thenReturn(null);

    UpdatePasswordRequestDto requestDto =
            new UpdatePasswordRequestDto("ui", "@Test12345");

    when(passwordEncoder.encode(anyString())).thenReturn(requestDto.getPwd());

    assertThrows(MemberPwdChangeFailedException.class, () -> memberService.updatePassword(requestDto));
  }

  @Test
  @DisplayName("회원 탈퇴")
  void deleteUser() {
    Member member = MemberDummy.dummy();
    Link link = new Link(1L, member, "url");
    Blog blog = BlogDummy.dummy(member);
    ReflectionTestUtils.setField(member, "links", List.of(link));
    ReflectionTestUtils.setField(member, "blogs", List.of(blog));

    when(memberRepository.findById(any())).thenReturn(Optional.of(member));
    when(passwordEncoder.matches(anyString(), anyString())).thenReturn(true);
    doNothing().when(linkRepository).deleteById(anyLong());
    when(followRepository.findRelatedByMemberNo(any())).thenReturn(List.of());
    doNothing().when(followRepository).deleteById(anyLong());
    when(blogRepository.findById(anyLong())).thenReturn(Optional.of(blog));

    memberService.deleteUser(member.getMemberNo(), member.getPassword());

    assertTrue(member.isLeft());
    assertTrue(blog.isDeleted());
  }

  @Test
  @DisplayName("회원 탈퇴 살패 - 비밀번호 미일치")
  void deleteUserFailed() {
    Member member = MemberDummy.dummy();
    Link link = new Link(1L, member, "url");
    Blog blog = BlogDummy.dummy(member);
    ReflectionTestUtils.setField(member, "links", List.of(link));
    ReflectionTestUtils.setField(member, "blogs", List.of(blog));

    when(memberRepository.findById(any())).thenReturn(Optional.of(member));
    when(passwordEncoder.matches(anyString(), anyString())).thenReturn(false);
    doNothing().when(linkRepository).deleteById(anyLong());
    when(followRepository.findRelatedByMemberNo(any())).thenReturn(List.of());
    doNothing().when(followRepository).deleteById(anyLong());
    when(blogRepository.findById(anyLong())).thenReturn(Optional.of(blog));

    assertThrows(AuthenticationException.class,
            () -> memberService.deleteUser(1, "@Test12345"));
  }
}