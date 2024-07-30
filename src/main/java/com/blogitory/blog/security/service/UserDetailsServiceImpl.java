package com.blogitory.blog.security.service;

import com.blogitory.blog.member.entity.Member;
import com.blogitory.blog.member.repository.MemberRepository;
import com.blogitory.blog.role.repository.RoleRepository;
import com.blogitory.blog.security.exception.AuthenticationException;
import com.blogitory.blog.security.users.UserDetailsImpl;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementation of UserDetailsService.
 *
 * @author woonseok
 * @since 1.0
 **/
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
  private final MemberRepository memberRepository;
  private final RoleRepository roleRepository;

  @Override
  public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
    Member member = memberRepository.findByEmail(email)
            .orElseThrow(() -> new AuthenticationException(
                    this.getClass().getName() + ": LoadUserByUsername failed"));

    List<SimpleGrantedAuthority> roles = roleRepository.findRolesByMemberNo(member.getMemberNo())
            .stream().map(SimpleGrantedAuthority::new).toList();

    return new UserDetailsImpl(
            member.getEmail(),
            member.getPassword(),
            member.getMemberNo(),
            member.getUsername(),
            member.getName(),
            member.getProfileThumb(),
            roles);
  }
}
