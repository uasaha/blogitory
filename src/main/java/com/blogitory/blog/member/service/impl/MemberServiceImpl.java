package com.blogitory.blog.member.service.impl;

import static com.blogitory.blog.blog.service.impl.BlogServiceImpl.BLOG_DELETED;

import com.blogitory.blog.blog.dto.response.GetBlogInProfileResponseDto;
import com.blogitory.blog.blog.entity.Blog;
import com.blogitory.blog.blog.repository.BlogRepository;
import com.blogitory.blog.commons.exception.NotFoundException;
import com.blogitory.blog.follow.entity.Follow;
import com.blogitory.blog.follow.repository.FollowRepository;
import com.blogitory.blog.jwt.service.JwtService;
import com.blogitory.blog.link.entity.Link;
import com.blogitory.blog.link.repository.LinkRepository;
import com.blogitory.blog.member.dto.request.LoginMemberRequestDto;
import com.blogitory.blog.member.dto.request.SignupMemberRequestDto;
import com.blogitory.blog.member.dto.request.UpdateMemberProfileRequestDto;
import com.blogitory.blog.member.dto.request.UpdatePasswordRequestDto;
import com.blogitory.blog.member.dto.response.GetMemberAlertInSettingsResponseDto;
import com.blogitory.blog.member.dto.response.GetMemberPersistInfoDto;
import com.blogitory.blog.member.dto.response.GetMemberProfileInSettingsResponseDto;
import com.blogitory.blog.member.dto.response.GetMemberProfileLinkResponseDto;
import com.blogitory.blog.member.dto.response.GetMemberProfileResponseDto;
import com.blogitory.blog.member.dto.response.LoginMemberResponseDto;
import com.blogitory.blog.member.entity.Member;
import com.blogitory.blog.member.exception.MemberEmailAlreadyUsedException;
import com.blogitory.blog.member.exception.MemberPwdChangeFailedException;
import com.blogitory.blog.member.repository.MemberRepository;
import com.blogitory.blog.member.service.MemberService;
import com.blogitory.blog.role.entity.Role;
import com.blogitory.blog.role.repository.RoleRepository;
import com.blogitory.blog.rolemember.entity.RoleMember;
import com.blogitory.blog.rolemember.repository.RoleMemberRepository;
import com.blogitory.blog.security.exception.AuthenticationException;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
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
@Slf4j
@RequiredArgsConstructor
@Transactional
public class MemberServiceImpl implements MemberService {
  private final MemberRepository memberRepository;
  private final RoleRepository roleRepository;
  private final RoleMemberRepository roleMemberRepository;
  private final FollowRepository followRepository;
  private final LinkRepository linkRepository;
  private final BlogRepository blogRepository;
  private final JwtService jwtService;
  private final PasswordEncoder passwordEncoder;
  private final RedisTemplate<String, Object> redisTemplate;

  private static final String MEMBER_DELETED = "M-D-";
  private static final Integer DEFAULT_ROLE_NO = 4;
  public static final String DEFAULT_THUMB = "/static/icons/person.svg";

  /**
   * {@inheritDoc}
   */
  @Override
  public void signup(SignupMemberRequestDto requestDto) {
    if (memberRepository.existsMemberByEmail(requestDto.getEmail())
            || memberRepository.existsMemberByUsername(requestDto.getUsername())) {
      throw new MemberEmailAlreadyUsedException(requestDto.getEmail());
    }

    Member member = Member.builder()
            .username(requestDto.getUsername())
            .email(requestDto.getEmail())
            .password(passwordEncoder.encode(requestDto.getPwd()))
            .name(requestDto.getName())
            .followAlert(true)
            .commentAlert(true)
            .heartAlert(true)
            .newAlert(true)
            .profileThumb(DEFAULT_THUMB)
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
  @Transactional(readOnly = true)
  public boolean existMemberByEmail(String email) {
    return memberRepository.existsMemberByEmail(email);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String login(LoginMemberRequestDto requestDto) {
    Member member = memberRepository.findByEmail(requestDto.getEmail())
            .orElseThrow(() -> new NotFoundException(Member.class));

    if (!passwordEncoder.matches(requestDto.getPassword(), member.getPassword())) {
      throw new AuthenticationException("Passwords do not match");
    }

    List<String> roles = roleRepository.findRolesByMemberNo(member.getMemberNo());

    LoginMemberResponseDto responseDto = new LoginMemberResponseDto(
            member.getMemberNo(),
            member.getEmail(),
            member.getUsername(),
            member.getName(),
            member.getProfileThumb(),
            member.getPassword(),
            roles);

    String uuid = UUID.randomUUID().toString();

    return jwtService.issue(uuid, responseDto);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  @Transactional(readOnly = true)
  public GetMemberPersistInfoDto persistInfo(Integer memberNo) {
    return memberRepository.getPersistInfo(memberNo)
            .orElseThrow(() -> new NotFoundException(Member.class));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  @Transactional(readOnly = true)
  public Boolean isDuplicateUsername(String username) {
    return memberRepository.existsMemberByUsername(username);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  @Transactional(readOnly = true)
  public String getPasswordByEmail(String email) {
    return memberRepository.findByEmail(email)
            .orElseThrow(() -> new NotFoundException(Member.class))
            .getPassword();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  @Transactional(readOnly = true)
  public String getThumbnailByNo(Integer memberNo) {
    return memberRepository.findProfileThumbByMemberNo(memberNo);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  @Transactional(readOnly = true)
  public GetMemberProfileResponseDto getProfileByUsername(String username) {
    Member member = memberRepository.findByUsername(username)
            .orElseThrow(() -> new NotFoundException(Member.class));

    if (member.isLeft() || member.isBlocked()) {
      throw new NotFoundException(Member.class);
    }

    Long follower = followRepository.countFollower(member.getMemberNo());
    Long followee = followRepository.countFollowee(member.getMemberNo());

    return new GetMemberProfileResponseDto(member.getUsername(),
            member.getName(),
            member.getBio(),
            member.getProfileThumb(),
            member.getIntroEmail(),
            member.getLinks().stream().map(link ->
                    new GetMemberProfileLinkResponseDto(link.getLinkNo(), link.getUrl())).toList(),
            member.getBlogs().stream().filter(blog -> !blog.isDeleted()).map(blog ->
                            new GetBlogInProfileResponseDto(blog.getUrlName(),
                                    blog.getName(), blog.getBio()))
                    .toList(),
            follower,
            followee);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void updateProfile(Integer memberNo, UpdateMemberProfileRequestDto requestDto) {
    Member member = memberRepository.findById(memberNo)
            .orElseThrow(() -> new NotFoundException(Member.class));

    member.updateName(requestDto.getName());
    member.updateBio(requestDto.getBio());
    member.updateOpenEmail(requestDto.getEmail());

    for (Link link : member.getLinks()) {
      linkRepository.deleteById(link.getLinkNo());
    }

    for (String newLinkUrl : requestDto.getLinkList()) {
      if (!newLinkUrl.isEmpty()) {
        Link link = new Link(null, member, newLinkUrl);
        linkRepository.save(link);
      }
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  @Transactional(readOnly = true)
  public GetMemberProfileInSettingsResponseDto getSettingsProfile(Integer memberNo) {
    Member member = memberRepository.findById(memberNo)
            .orElseThrow(() -> new NotFoundException(Member.class));

    return new GetMemberProfileInSettingsResponseDto(
            member.getUsername(),
            member.getName(),
            member.getProfileThumb(),
            member.getEmail(),
            member.getBio(),
            member.getIntroEmail(),
            member.getLinks().stream().map(Link::getUrl).toList()
    );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  @Transactional(readOnly = true)
  public GetMemberAlertInSettingsResponseDto getSettingsAlert(Integer memberNo) {
    Member member = memberRepository.findById(memberNo)
            .orElseThrow(() -> new NotFoundException(Member.class));

    return new GetMemberAlertInSettingsResponseDto(
            member.isFollowAlert(),
            member.isCommentAlert(),
            member.isHeartAlert(),
            member.isNewAlert());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void updateAlerts(Integer memberNo, String type, boolean isOn) {
    Member member = memberRepository.findById(memberNo)
            .orElseThrow(() -> new NotFoundException(Member.class));

    if (AlertType.COMMENT.equals(AlertType.valueOf(type))) {
      member.updateCommentAlert(isOn);
    }

    if (AlertType.FOLLOW.equals(AlertType.valueOf(type))) {
      member.updateFollowAlert(isOn);
    }

    if (AlertType.NEW_POSTS.equals(AlertType.valueOf(type))) {
      member.updateNewAlert(isOn);
    }

    if (AlertType.HEART.equals(AlertType.valueOf(type))) {
      member.updateHeartAlert(isOn);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void updatePassword(UpdatePasswordRequestDto requestDto) {
    String email = (String) redisTemplate.opsForValue().getAndDelete(requestDto.getUi());

    if (email == null) {
      throw new MemberPwdChangeFailedException();
    }

    Member member = memberRepository.findByEmail(email)
            .orElseThrow(MemberPwdChangeFailedException::new);

    String newPassword = requestDto.getPwd();

    member.updatePassword(passwordEncoder.encode(newPassword));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void deleteUser(Integer memberNo, String pwd) {
    Member member = memberRepository.findById(memberNo)
            .orElseThrow(() -> new NotFoundException(Member.class));

    if (!passwordEncoder.matches(pwd, member.getPassword())) {
      throw new AuthenticationException("탈퇴 - 비밀번호가 일치하지 않습니다.");
    }

    member.getLinks().forEach(link -> linkRepository.deleteById(link.getLinkNo()));

    List<Follow> follows = followRepository.findRelatedByMemberNo(memberNo);
    follows.forEach(f -> followRepository.deleteById(f.getFollowNo()));

    member.getBlogs().forEach(b -> {
      Blog blog = blogRepository.findById(b.getBlogNo())
              .orElseThrow(() -> new NotFoundException(Blog.class));

      String blogMsg = BLOG_DELETED + UUID.randomUUID();
      blog.quitBlog(blogMsg);
    });

    String msg = MEMBER_DELETED + UUID.randomUUID();

    member.deleteMember(msg);
  }

  private enum AlertType {
    COMMENT,
    FOLLOW,
    NEW_POSTS,
    HEART
  }
}
