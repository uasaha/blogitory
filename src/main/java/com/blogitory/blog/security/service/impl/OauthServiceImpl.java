package com.blogitory.blog.security.service.impl;

import static com.blogitory.blog.member.service.impl.MemberServiceImpl.DEFAULT_ROLE_NO;

import com.blogitory.blog.commons.exception.NotFoundException;
import com.blogitory.blog.jwt.service.JwtService;
import com.blogitory.blog.member.dto.request.SignupOauthMemberRequestDto;
import com.blogitory.blog.member.dto.response.LoginMemberResponseDto;
import com.blogitory.blog.member.entity.Member;
import com.blogitory.blog.member.repository.MemberRepository;
import com.blogitory.blog.role.entity.Role;
import com.blogitory.blog.role.repository.RoleRepository;
import com.blogitory.blog.rolemember.entity.RoleMember;
import com.blogitory.blog.rolemember.repository.RoleMemberRepository;
import com.blogitory.blog.security.adaptor.OauthAdaptor;
import com.blogitory.blog.security.dto.GithubAccessTokenResponseDto;
import com.blogitory.blog.security.dto.GithubUserInfoResponseDto;
import com.blogitory.blog.security.exception.AuthenticationException;
import com.blogitory.blog.security.exception.NotFoundOauthUser;
import com.blogitory.blog.security.service.OauthService;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * Oauth Service.
 *
 * @author woonseok
 * @since 1.0
 **/
@Service
@RequiredArgsConstructor
public class OauthServiceImpl implements OauthService {
  private final OauthAdaptor oauthAdaptor;
  private final MemberRepository memberRepository;
  private final RoleRepository roleRepository;
  private final JwtService jwtService;
  private final RoleMemberRepository roleMemberRepository;

  private static final String GITHUB = "github";

  /**
   * {@inheritDoc}
   */
  @Override
  public void oauthSignup(SignupOauthMemberRequestDto requestDto) {
    String profileImg = "";

    if (requestDto.getProfileThumb() != null && !requestDto.getProfileThumb().isEmpty()) {
      profileImg = requestDto.getProfileThumb();
    }

    Member member = Member.builder()
            .email(GITHUB + requestDto.getId())
            .password(UUID.randomUUID().toString())
            .username(requestDto.getUsername())
            .name(requestDto.getName())
            .oauth(GITHUB)
            .profileThumb(profileImg)
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
  public String getGithubAccessToken(String code) {
    GithubAccessTokenResponseDto accessTokenResponseDto = oauthAdaptor.getGithubAccessToken(code);

    if (Objects.isNull(accessTokenResponseDto)
            || Objects.isNull(accessTokenResponseDto.getAccessToken())) {
      throw new AuthenticationException("Github access token response is null.");
    }

    return accessTokenResponseDto.getAccessToken();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String githubLogin(String githubAccessToken) {
    GithubUserInfoResponseDto userInfo = oauthAdaptor.getGithubUserInfo(githubAccessToken);

    Optional<Member> member = memberRepository.findByOauthId(GITHUB, GITHUB + userInfo.getId());

    if (member.isEmpty() || !member.get().getOauth().equals(GITHUB)) {
      throw new NotFoundOauthUser(GITHUB, userInfo.getId(),
              userInfo.getName(), userInfo.getAvatarUrl());
    }

    return getAccessToken(member.get());
  }

  private String getAccessToken(Member member) {
    List<String> roles = roleRepository.findRolesByMemberNo(member.getMemberNo());

    LoginMemberResponseDto loginMemberResponseDto = new LoginMemberResponseDto(
            member.getMemberNo(),
            member.getEmail(),
            member.getUsername(),
            member.getName(),
            member.getProfileThumb(),
            member.getPassword(),
            roles);

    UUID uuid = UUID.randomUUID();

    return jwtService.issue(uuid.toString(), loginMemberResponseDto);
  }
}