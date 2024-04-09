package com.blogitory.blog.member.repository;

import static org.junit.jupiter.api.Assertions.*;

import com.blogitory.blog.member.entity.Member;
import com.blogitory.blog.member.entity.MemberDummy;
import com.blogitory.blog.role.entity.Role;
import com.blogitory.blog.role.entity.RoleDummy;
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
 **/
@DataJpaTest
class MemberRepositoryTest {
  @Autowired
  MemberRepository memberRepository;

  Member member;
  Role role;

  @BeforeEach
  void beforeEach() {
    member = MemberDummy.dummy();
    role = RoleDummy.dummy();
  }

  @Test
  @DisplayName("회원 저장 성공")
  void memberSave() {
    Member savedMember = memberRepository.save(member);

    assertAll(
            () -> assertEquals(member.getMemberNo(), savedMember.getMemberNo()),
            () -> assertEquals(member.getEmail(), savedMember.getEmail()),
            () -> assertEquals(member.getPassword(), savedMember.getPassword()),
            () -> assertEquals(member.getName(), savedMember.getName()),
            () -> assertEquals(member.getProfileThumb(), savedMember.getProfileThumb()),
            () -> assertEquals(member.getIntroEmail(), savedMember.getIntroEmail()),
            () -> assertEquals(member.getGithub(), savedMember.getGithub()),
            () -> assertEquals(member.getTwitter(), savedMember.getTwitter()),
            () -> assertEquals(member.getFacebook(), savedMember.getFacebook()),
            () -> assertEquals(member.getHomepage(), savedMember.getHomepage()),
            () -> assertEquals(member.isBlocked(), savedMember.isBlocked()),
            () -> assertEquals(member.isLeft(), savedMember.isLeft())
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
}