package com.blogitory.blog.follow.repository;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.blogitory.blog.follow.entity.Follow;
import com.blogitory.blog.follow.entity.FollowDummy;
import com.blogitory.blog.member.entity.Member;
import com.blogitory.blog.member.entity.MemberDummy;
import com.blogitory.blog.member.repository.MemberRepository;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

/**
 * Follow repository test.
 *
 * @author woonseok
 * @since 1.0
 **/
@DataJpaTest
class FollowRepositoryTest {

  @Autowired
  FollowRepository followRepository;

  @Autowired
  MemberRepository memberRepository;

  @Autowired
  EntityManager entityManager;

  @BeforeEach
  void setUp() {
  }

  @AfterEach
  void teardown() {
    entityManager.createNativeQuery("ALTER TABLE `member` ALTER COLUMN `member_no` RESTART")
            .executeUpdate();
    entityManager.createNativeQuery("ALTER TABLE `follow` ALTER COLUMN `member_no` RESTART")
            .executeUpdate();
  }

//  @Test
//  @DisplayName("Follow 저장")
//  void followSave() {
//    Member follower = new Member(
//              null,
//            "follower@email.com",
//            "password",
//            "follower",
//            "leader_profile",
//            "intro@email.com",
//            "github",
//            "twitter",
//            "facebook",
//            "homepage",
//            false,
//            false
//    );
//    follower = memberRepository.save(follower);
//
//    Member leader = new Member(
//            null,
//            "leader@email.com",
//            "password",
//            "leader",
//            "leader_profile",
//            "intro@email.com",
//            "gitgit",
//            "twitwit",
//            "faceface",
//            "home",
//            false,
//            false);
//    leader = memberRepository.save(leader);
//
//    Follow follow = FollowDummy.dummy(follower, leader);
//    Follow actual = followRepository.save(follow);
//
//    assertAll(
//            () -> assertEquals(follow.getFollower().getMemberNo(), actual.getFollower().getMemberNo()),
//            () -> assertEquals(follow.getLeader().getMemberNo(), actual.getLeader().getMemberNo()),
//            () -> assertEquals(follow.getFollowNo(), actual.getFollowNo())
//    );
//  }
}