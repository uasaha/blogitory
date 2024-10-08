package com.blogitory.blog.rolemember.repository;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.blogitory.blog.commons.config.JpaConfig;
import com.blogitory.blog.commons.config.QuerydslConfig;
import com.blogitory.blog.member.entity.Member;
import com.blogitory.blog.member.entity.MemberDummy;
import com.blogitory.blog.member.repository.MemberRepository;
import com.blogitory.blog.role.entity.Role;
import com.blogitory.blog.role.entity.RoleDummy;
import com.blogitory.blog.role.repository.RoleRepository;
import com.blogitory.blog.rolemember.entity.RoleMember;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

/**
 * RoleMemberRepository test.
 *
 * @author woonseok
 * @since 1.0
 */
@Import({JpaConfig.class, QuerydslConfig.class})
@DataJpaTest
class RoleMemberRepositoryTest {

  /**
   * The Role member repository.
   */
  @Autowired
  RoleMemberRepository roleMemberRepository;

  /**
   * The Member repository.
   */
  @Autowired
  MemberRepository memberRepository;

  /**
   * The Role repository.
   */
  @Autowired
  RoleRepository roleRepository;

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
   * Sets up.
   */
  @BeforeEach
  void setUp() {
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
    entityManager.createNativeQuery("ALTER TABLE `role` ALTER COLUMN `role_no` RESTART")
            .executeUpdate();
  }

  /**
   * Role member save.
   */
  @Test
  @DisplayName("회원권한 저장 성공")
  void roleMemberSave() {
    member = memberRepository.save(member);
    role = roleRepository.save(role);

    RoleMember roleMember = new RoleMember(role, member);

    RoleMember actual = roleMemberRepository.save(roleMember);

    assertAll(
            () -> assertEquals(role.getRoleName(), actual.getRole().getRoleName()),
            () -> assertEquals(role.getRoleNo(), actual.getRole().getRoleNo()),
            () -> assertEquals(role.getRoleNo(), actual.getPk().getRoleNo()),
            () -> assertEquals(member.getMemberNo(), actual.getMember().getMemberNo()),
            () -> assertEquals(member.getMemberNo(), actual.getPk().getMemberNo()),
            () -> assertEquals(roleMember.getPk(), actual.getPk())
    );
  }
}