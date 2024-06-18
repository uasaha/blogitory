package com.blogitory.blog.member.repository;

import static org.junit.jupiter.api.Assertions.*;

import com.blogitory.blog.member.dto.MemberMyProfileResponseDto;
import com.blogitory.blog.member.dto.MemberPersistInfoDto;
import com.blogitory.blog.member.entity.Member;
import com.blogitory.blog.member.entity.MemberDummy;
import com.blogitory.blog.role.entity.Role;
import com.blogitory.blog.role.entity.RoleDummy;
import jakarta.persistence.EntityManager;
import java.util.Optional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

/**
 * MemberRepository test.
 *
 * @author woonseok
 * @since 1.0
 */
@DataJpaTest
class MemberRepositoryTest {
  /**
   * The Member repository.
   */
  @Autowired
  MemberRepository memberRepository;

  /**
   * The Entity manager.
   */
  @Autowired
  EntityManager entityManager;

  /**
   * The Member.
   */
  Member member;
  /**
   * The Role.
   */
  Role role;

  /**
   * Before each.
   */
  @BeforeEach
  void beforeEach() {
    member = MemberDummy.dummy();
    role = RoleDummy.dummy();
  }

  /**
   * Teardown.
   */
  @AfterEach
  void teardown() {
    entityManager.createNativeQuery("ALTER TABLE `member` ALTER COLUMN `member_no` RESTART")
            .executeUpdate();
  }

  /**
   * Member save.
   */
  @Test
  @DisplayName("회원 저장 성공")
  void memberSave() {
    Member savedMember = memberRepository.save(member);

    assertAll(
            () -> assertEquals(member.getMemberNo(), savedMember.getMemberNo()),
            () -> assertEquals(member.getEmail(), savedMember.getEmail()),
            () -> assertEquals(member.getPassword(), savedMember.getPassword()),
            () -> assertEquals(member.getUsername(), savedMember.getUsername()),
            () -> assertEquals(member.getName(), savedMember.getName()),
            () -> assertEquals(member.getProfileThumb(), savedMember.getProfileThumb()),
            () -> assertEquals(member.getIntroEmail(), savedMember.getIntroEmail()),
            () -> assertEquals(member.getGithub(), savedMember.getGithub()),
            () -> assertEquals(member.getTwitter(), savedMember.getTwitter()),
            () -> assertEquals(member.getFacebook(), savedMember.getFacebook()),
            () -> assertEquals(member.getHomepage(), savedMember.getHomepage()),
            () -> assertEquals(member.isBlocked(), savedMember.isBlocked()),
            () -> assertEquals(member.isLeft(), savedMember.isLeft()),
            () -> assertEquals(member.isOauth(), savedMember.isOauth())
    );
  }

  /**
   * Exists member by email.
   */
  @Test
  @DisplayName("회원 이메일 중복 조회")
  void existsMemberByEmail() {
    memberRepository.save(member);

    boolean existResult = memberRepository.existsMemberByEmail(member.getEmail());
    boolean notExistResult = memberRepository.existsMemberByEmail("notExist@not.com");

    assertTrue(existResult);
    assertFalse(notExistResult);
  }

  /**
   * Gets my profile.
   */
  @Test
  @DisplayName("내 프로필 조회")
  void getMyProfile() {
    memberRepository.save(member);

    Optional<MemberMyProfileResponseDto> responseDto =
            memberRepository.getMyProfile(member.getMemberNo());

    assertTrue(responseDto.isPresent());

    MemberMyProfileResponseDto actual = responseDto.orElseThrow();

    assertAll(
            () -> assertEquals(member.getEmail(), actual.getEmail()),
            () -> assertEquals(member.getName(), actual.getName()),
            () -> assertEquals(member.getProfileThumb(), actual.getProfileThumb()),
            () -> assertEquals(member.getIntroEmail(), actual.getIntroEmail()),
            () -> assertEquals(member.getGithub(), actual.getGithub()),
            () -> assertEquals(member.getTwitter(), actual.getTwitter()),
            () -> assertEquals(member.getFacebook(), actual.getFacebook()),
            () -> assertEquals(member.getHomepage(), actual.getHomepage()),
            () -> assertEquals(member.getCreatedAt(), actual.getCreatedAt())
    );
  }

  /**
   * Gets persist info.
   */
  @Test
  @DisplayName("로그인 정보 조회")
  void getPersistInfo() {
    memberRepository.save(member);

    Optional<MemberPersistInfoDto> responseDto =
            memberRepository.getPersistInfo(member.getMemberNo());

    assertTrue(responseDto.isPresent());

    MemberPersistInfoDto actual = responseDto.orElseThrow();

    assertAll(
            () -> assertEquals(member.getName(), actual.getName()),
            () -> assertEquals(member.getProfileThumb(), actual.getThumb())
    );
  }
}