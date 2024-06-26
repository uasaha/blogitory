package com.blogitory.blog.follow.repository;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.blogitory.blog.follow.entity.Follow;
import com.blogitory.blog.follow.entity.FollowDummy;
import com.blogitory.blog.member.entity.Member;
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
 */
@DataJpaTest
class FollowRepositoryTest {

  /**
   * The Follow repository.
   */
  @Autowired
  FollowRepository followRepository;

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
   * Sets up.
   */
  @BeforeEach
  void setUp() {
  }

  /**
   * Teardown.
   */
  @AfterEach
  void teardown() {
    entityManager.createNativeQuery("ALTER TABLE `member` ALTER COLUMN `member_no` RESTART")
            .executeUpdate();
    entityManager.createNativeQuery("ALTER TABLE `follow` ALTER COLUMN `follow_no` RESTART")
            .executeUpdate();
  }

  /**
   * Follow save.
   */
  @Test
  @DisplayName("Follow 저장")
  void followSave() {
    assertTrue(true);
    Member follower = new Member(
              1,
            "follower@email.com",
            "password",
            "follower",
            "follower",
            "follower",
            "leader_profile",
            "intro@email.com",
            false,
            false,
            false
    );
    follower = memberRepository.save(follower);

    Member leader = new Member(
            2,
            "leader@email.com",
            "password",
            "leader",
            "leader",
            "leader",
            "leader_profile",
            "intro@email.com",
            false,
            false,
            false
    );
    leader = memberRepository.save(leader);

    Follow follow = FollowDummy.dummy(follower, leader);
    Follow actual = followRepository.save(follow);

    assertAll(
            () -> assertEquals(follow.getFollowTo().getMemberNo(), actual.getFollowTo().getMemberNo()),
            () -> assertEquals(follow.getFollowFrom().getMemberNo(), actual.getFollowFrom().getMemberNo()),
            () -> assertEquals(follow.getFollowNo(), actual.getFollowNo())
    );
  }
}