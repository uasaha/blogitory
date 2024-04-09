package com.blogitory.blog.member.service.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.blogitory.blog.commons.exception.NotFoundException;
import com.blogitory.blog.member.dto.MemberSignupRequestDto;
import com.blogitory.blog.member.entity.Member;
import com.blogitory.blog.member.entity.MemberDummy;
import com.blogitory.blog.member.repository.MemberRepository;
import com.blogitory.blog.member.service.MemberService;
import com.blogitory.blog.role.entity.Role;
import com.blogitory.blog.role.repository.RoleRepository;
import com.blogitory.blog.rolemember.entity.RoleMember;
import com.blogitory.blog.rolemember.entity.RoleMemberDummy;
import com.blogitory.blog.rolemember.repository.RoleMemberRepository;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.password.PasswordEncoder;

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

  @BeforeEach
  void setUp() {
    memberRepository = mock(MemberRepository.class);
    roleRepository = mock(RoleRepository.class);
    roleMemberRepository = mock(RoleMemberRepository.class);
    passwordEncoder = mock(PasswordEncoder.class);

    memberService = new MemberServiceImpl(
            memberRepository, roleRepository, roleMemberRepository, passwordEncoder);
  }

  @Test
  @DisplayName("회원가입")
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
  void signupFail() {
    Member member = MemberDummy.dummy();
    Role role = new Role(4, "ROLE_DUMMY");
    RoleMember roleMember = RoleMemberDummy.dummy(role, member);
    MemberSignupRequestDto requestDto = new MemberSignupRequestDto(
            member.getName(), member.getEmail(), member.getPassword());

    when(memberRepository.save(any())).thenReturn(member);
    when(roleRepository.findById(4)).thenThrow(NotFoundException.class);
    when(roleMemberRepository.save(any())).thenReturn(roleMember);
    when(passwordEncoder.encode(member.getPassword())).thenReturn(member.getPassword());

    assertThrows(NotFoundException.class, () -> memberService.signup(requestDto));
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
}