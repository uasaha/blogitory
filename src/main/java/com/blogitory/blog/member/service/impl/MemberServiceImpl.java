package com.blogitory.blog.member.service.impl;

import com.blogitory.blog.commons.exception.NotFoundException;
import com.blogitory.blog.jwt.service.JwtService;
import com.blogitory.blog.member.dto.MemberLoginRequestDto;
import com.blogitory.blog.member.dto.MemberLoginResponseDto;
import com.blogitory.blog.member.dto.MemberMyProfileResponseDto;
import com.blogitory.blog.member.dto.MemberPersistInfoDto;
import com.blogitory.blog.member.dto.MemberSignupRequestDto;
import com.blogitory.blog.member.dto.MemberUpdateNameRequestDto;
import com.blogitory.blog.member.dto.MemberUpdateProfileRequestDto;
import com.blogitory.blog.member.entity.Member;
import com.blogitory.blog.member.exception.MemberEmailAlreadyUsedException;
import com.blogitory.blog.member.repository.MemberRepository;
import com.blogitory.blog.member.service.MemberService;
import com.blogitory.blog.role.entity.Role;
import com.blogitory.blog.role.repository.RoleRepository;
import com.blogitory.blog.rolemember.entity.RoleMember;
import com.blogitory.blog.rolemember.repository.RoleMemberRepository;
import com.blogitory.blog.security.exception.AuthenticationException;
import java.util.UUID;
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
  private final JwtService jwtService;

  private final PasswordEncoder passwordEncoder;

  private static final Integer DEFAULT_ROLE_NO = 4;

  /**
   * {@inheritDoc}
   */
  @Override
  @Transactional
  public void signup(MemberSignupRequestDto requestDto) {
    if (memberRepository.existsMemberByEmail(requestDto.getEmail())) {
      throw new MemberEmailAlreadyUsedException(requestDto.getEmail());
    }

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

  /**
   * {@inheritDoc}
   */
  @Override
  @Transactional
  public String login(MemberLoginRequestDto requestDto) {
    Member member = memberRepository.findByEmail(requestDto.getEmail())
            .orElseThrow(AuthenticationException::new);

    if (!passwordEncoder.matches(requestDto.getPassword(), member.getPassword())) {
      throw new AuthenticationException();
    }

    MemberLoginResponseDto responseDto = new MemberLoginResponseDto(
            member.getMemberNo(),
            member.getEmail(),
            member.getName(),
            member.getPassword());

    String uuid = UUID.randomUUID().toString();

    return jwtService.issue(uuid, responseDto,
            roleRepository.findRolesByMemberNo(member.getMemberNo()));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public MemberMyProfileResponseDto myProfile(Integer memberNo) {
    return memberRepository.getMyProfile(memberNo)
            .orElseThrow(() -> new NotFoundException(Member.class));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public MemberPersistInfoDto persistInfo(Integer memberNo) {
    return memberRepository.getPersistInfo(memberNo)
            .orElseThrow(() -> new NotFoundException(Member.class));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  @Transactional
  public void updateName(Integer memberNo, MemberUpdateNameRequestDto requestDto) {
    Member member = memberRepository.findById(memberNo)
            .orElseThrow(() -> new NotFoundException(Member.class));

    member.updateName(requestDto.getName());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  @Transactional
  public void updateOpenEmail(Integer memberNo, MemberUpdateProfileRequestDto requestDto) {
    Member member = memberRepository.findById(memberNo)
            .orElseThrow(() -> new NotFoundException(Member.class));

    member.updateOpenEmail(requestDto.getContent());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  @Transactional
  public void updateGithub(Integer memberNo, MemberUpdateProfileRequestDto requestDto) {
    Member member = memberRepository.findById(memberNo)
            .orElseThrow(() -> new NotFoundException(Member.class));

    member.updateGithub(requestDto.getContent());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  @Transactional
  public void updateFacebook(Integer memberNo, MemberUpdateProfileRequestDto requestDto) {
    Member member = memberRepository.findById(memberNo)
            .orElseThrow(() -> new NotFoundException(Member.class));

    member.updateFacebook(requestDto.getContent());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  @Transactional
  public void updateX(Integer memberNo, MemberUpdateProfileRequestDto requestDto) {
    Member member = memberRepository.findById(memberNo)
            .orElseThrow(() -> new NotFoundException(Member.class));

    member.updateX(requestDto.getContent());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  @Transactional
  public void updateHomepage(Integer memberNo, MemberUpdateProfileRequestDto requestDto) {
    Member member = memberRepository.findById(memberNo)
            .orElseThrow(() -> new NotFoundException(Member.class));

    member.updateHomepage(requestDto.getContent());
  }
}
