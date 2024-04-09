package com.blogitory.blog.member.service.impl;

import com.blogitory.blog.commons.exception.NotFoundException;
import com.blogitory.blog.member.dto.MemberSignupRequestDto;
import com.blogitory.blog.member.entity.Member;
import com.blogitory.blog.member.repository.MemberRepository;
import com.blogitory.blog.member.service.MemberService;
import com.blogitory.blog.role.entity.Role;
import com.blogitory.blog.role.repository.RoleRepository;
import com.blogitory.blog.rolemember.entity.RoleMember;
import com.blogitory.blog.rolemember.repository.RoleMemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * An Implementation of MemberService.
 *
 * @author woonseok
 * @since 1.0
 **/
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberServiceImpl implements MemberService {
  private final MemberRepository memberRepository;
  private final RoleRepository roleRepository;
  private final RoleMemberRepository roleMemberRepository;

  private final PasswordEncoder passwordEncoder;

  private static final Integer DEFAULT_ROLE_NO = 4;

  /**
   * {@inheritDoc}
   */
  @Override
  @Transactional
  public void signup(MemberSignupRequestDto requestDto) {
    Member member = Member.builder()
            .email(requestDto.getEmail())
            .password(passwordEncoder.encode(requestDto.getPwd()))
            .name(requestDto.getName())
            .build();

    member = memberRepository.save(member);
    Role role = roleRepository.findById(DEFAULT_ROLE_NO)
            .orElseThrow(() -> new NotFoundException(Role.class));

    roleMemberRepository.save(new RoleMember(role, member));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean existMemberByEmail(String email) {
    return memberRepository.existsMemberByEmail(email);
  }
}
