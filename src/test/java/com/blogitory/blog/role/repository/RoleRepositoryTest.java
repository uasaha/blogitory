package com.blogitory.blog.role.repository;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.blogitory.blog.member.entity.Member;
import com.blogitory.blog.member.entity.MemberDummy;
import com.blogitory.blog.member.repository.MemberRepository;
import com.blogitory.blog.role.entity.Role;
import com.blogitory.blog.role.entity.RoleDummy;
import com.blogitory.blog.rolemember.entity.RoleMember;
import com.blogitory.blog.rolemember.entity.RoleMemberDummy;
import com.blogitory.blog.rolemember.repository.RoleMemberRepository;
import jakarta.persistence.EntityManager;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

/**
 * Role repository test.
 *
 * @author woonseok
 * @since 1.0
 **/
@DataJpaTest
class RoleRepositoryTest {

  @Autowired
  RoleRepository roleRepository;

  @Autowired
  MemberRepository memberRepository;

  @Autowired
  RoleMemberRepository roleMemberRepository;

  @Autowired
  EntityManager entityManager;

  @AfterEach
  void teardown() {
    entityManager.createNativeQuery("ALTER TABLE `role` ALTER COLUMN `role_no` RESTART")
            .executeUpdate();
    entityManager.createNativeQuery("ALTER TABLE `member` ALTER COLUMN `member_no` RESTART")
            .executeUpdate();
  }

  @Test
  @DisplayName("권한 저장")
  void roleSave() {
    Role role = RoleDummy.dummy();
    Role actual = roleRepository.save(role);

    assertAll(
            () -> assertEquals(role.getRoleNo(), actual.getRoleNo()),
            () -> assertEquals(role.getRoleName(), actual.getRoleName())
    );
  }

  @Test
  @DisplayName("회원으로 조회")
  void findRolesByMemberNo() {
    Member member = MemberDummy.dummy();
    member = memberRepository.save(member);

    Role role = RoleDummy.dummy();
    role = roleRepository.save(role);

    RoleMember roleMember = RoleMemberDummy.dummy(role, member);
    roleMemberRepository.save(roleMember);

    List<String> roles = roleRepository.findRolesByMemberNo(member.getMemberNo());

    assertNotNull(roles);

    Optional<String> actualRole = roles.stream().findFirst();

    assertTrue(actualRole.isPresent());

    String actual = actualRole.orElseThrow();

    assertEquals(role.getRoleName(), actual);
  }
}