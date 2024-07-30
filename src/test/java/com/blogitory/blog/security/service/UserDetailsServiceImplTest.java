package com.blogitory.blog.security.service;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.blogitory.blog.member.entity.Member;
import com.blogitory.blog.member.entity.MemberDummy;
import com.blogitory.blog.member.repository.MemberRepository;
import com.blogitory.blog.role.repository.RoleRepository;
import com.blogitory.blog.security.users.UserDetailsImpl;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * UserDetailsService test.
 *
 * @author woonseok
 * @since 1.0
 */
class UserDetailsServiceImplTest {
  /**
   * The Member repository.
   */
  MemberRepository memberRepository;
  /**
   * The Role repository.
   */
  RoleRepository roleRepository;
  /**
   * The Password encoder.
   */
  PasswordEncoder passwordEncoder;

  /**
   * Load user by username.
   */
  @Test
  void loadUserByUsername() {
    memberRepository = mock(MemberRepository.class);
    roleRepository = mock(RoleRepository.class);
    passwordEncoder = mock(PasswordEncoder.class);

    UserDetailsServiceImpl userDetailsService =
            new UserDetailsServiceImpl(memberRepository, roleRepository);

    Member member = MemberDummy.dummy();
    List<String> roles =
            List.of("ROLE_DUMMY");
    List<GrantedAuthority> grantedRoles =
            List.of(new SimpleGrantedAuthority("ROLE_DUMMY"));

    when(memberRepository.findByEmail(any())).thenReturn(Optional.of(member));
    when(roleRepository.findRolesByMemberNo(any())).thenReturn(roles);

    UserDetailsImpl expect = new UserDetailsImpl(
            member.getEmail(),
            member.getPassword(),
            member.getMemberNo(),
            member.getUsername(),
            member.getName(),
            member.getProfileThumb(),
            grantedRoles);

    UserDetails actual = userDetailsService.loadUserByUsername(member.getEmail());

    assertAll(
            () -> assertEquals(expect.getUsername(), actual.getUsername()),
            () -> assertEquals(expect.getPassword(), actual.getPassword()),
            () -> assertEquals(expect.getAuthorities(), actual.getAuthorities()),
            () -> assertTrue(expect.isAccountNonExpired()),
            () -> assertTrue(expect.isAccountNonLocked()),
            () -> assertTrue(expect.isCredentialsNonExpired()),
            () -> assertTrue(expect.isEnabled()),
            () -> assertEquals(expect.getIdName(), member.getUsername()),
            () -> assertEquals(expect.getName(), member.getName()),
            () -> assertEquals(expect.getUserNo(), member.getMemberNo())
    );
  }
}