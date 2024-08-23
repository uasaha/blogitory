package com.blogitory.blog.member.repository;

import static org.junit.jupiter.api.Assertions.*;

import com.blogitory.blog.commons.config.JpaConfig;
import com.blogitory.blog.commons.config.QuerydslConfig;
import com.blogitory.blog.member.dto.response.GetMemberPersistInfoDto;
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
import org.springframework.context.annotation.Import;
import org.springframework.test.util.ReflectionTestUtils;

/**
 * MemberRepository test.
 *
 * @author woonseok
 * @since 1.0
 */
@Import({JpaConfig.class, QuerydslConfig.class})
@DataJpaTest
class MemberRepositoryTest {

  @Autowired
  MemberRepository memberRepository;

  @Autowired
  EntityManager entityManager;

  Member member;

  Role role;

  @BeforeEach
  void beforeEach() {
    member = MemberDummy.dummy();
    role = RoleDummy.dummy();
  }

  @AfterEach
  void teardown() {
    entityManager.createNativeQuery("ALTER TABLE `member` ALTER COLUMN `member_no` RESTART")
            .executeUpdate();
  }

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
            () -> assertEquals(member.isBlocked(), savedMember.isBlocked()),
            () -> assertEquals(member.isLeft(), savedMember.isLeft()),
            () -> assertEquals(member.getOauth(), savedMember.getOauth()),
            () -> assertEquals(member.isHeartAlert(), savedMember.isHeartAlert()),
            () -> assertEquals(member.isCommentAlert(), savedMember.isCommentAlert()),
            () -> assertEquals(member.isNewAlert(), savedMember.isNewAlert()),
            () -> assertEquals(member.isFollowAlert(), savedMember.isFollowAlert())
    );
  }

  @Test
  @DisplayName("회원 이메일 중복 조회")
  void existsMemberByEmail() {
    memberRepository.save(member);

    boolean existResult = memberRepository.existsMemberByEmail(member.getEmail());
    boolean notExistResult = memberRepository.existsMemberByEmail("notExist@not.com");

    assertTrue(existResult);
    assertFalse(notExistResult);
  }

  @Test
  @DisplayName("로그인 정보 조회")
  void getPersistInfo() {
    memberRepository.save(member);

    Optional<GetMemberPersistInfoDto> responseDto =
            memberRepository.getPersistInfo(member.getMemberNo());

    assertTrue(responseDto.isPresent());

    GetMemberPersistInfoDto actual = responseDto.orElseThrow();

    assertAll(
            () -> assertEquals(member.getName(), actual.getName()),
            () -> assertEquals(member.getProfileThumb(), actual.getThumb())
    );
  }

  @Test
  @DisplayName("OAuth 가입자 정보 조회")
  void findByOauthId() {
    member = memberRepository.save(member);
    ReflectionTestUtils.setField(member, "oauth", "test");

    Optional<Member> actual = memberRepository.findByOauthId("test", member.getEmail());

    assertTrue(actual.isPresent());
  }
}