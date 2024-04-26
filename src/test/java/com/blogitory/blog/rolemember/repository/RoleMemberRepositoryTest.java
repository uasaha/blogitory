package com.blogitory.blog.rolemember.repository;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

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

/**
 * RoleMemberRepository test.
 *
 * @author woonseok
 * @since 1.0
 **/
@DataJpaTest
class RoleMemberRepositoryTest {

  @Autowired
  RoleMemberRepository roleMemberRepository;

  @Autowired
  MemberRepository memberRepository;

  @Autowired
  RoleRepository roleRepository;

  @Autowired
  EntityManager entityManager;

  Member member;
  Role role;
  @BeforeEach
  void setUp() {
    member = MemberDummy.dummy();
    role = RoleDummy.dummy();
  }

  @AfterEach
  void teardown() {
    entityManager.createNativeQuery("ALTER TABLE `member` ALTER COLUMN `member_no` RESTART")
            .executeUpdate();
    entityManager.createNativeQuery("ALTER TABLE `role` ALTER COLUMN `role_no` RESTART")
            .executeUpdate();
  }

  @Test
  @DisplayName("회원권한 저장 성공")
  void roleMemberSave() {
    member = memberRepository.save(member);
    role = roleRepository.save(role);

    RoleMember roleMember = new RoleMember(role, member);

    roleMember = roleMemberRepository.save(roleMember);

    RoleMember finalRoleMember = roleMember;

    assertAll(
            () -> assertEquals(role.getRoleName(), finalRoleMember.getRole().getRoleName()),
            () -> assertEquals(role.getRoleNo(), finalRoleMember.getRole().getRoleNo()),
            () -> assertEquals(role.getRoleNo(), finalRoleMember.getPk().getRoleNo()),
            () -> assertEquals(member.getMemberNo(), finalRoleMember.getMember().getMemberNo()),
            () -> assertEquals(member.getMemberNo(), finalRoleMember.getPk().getMemberNo())
    );
  }
}