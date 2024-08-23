package com.blogitory.blog.follow.repository;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.blogitory.blog.commons.config.JpaConfig;
import com.blogitory.blog.commons.config.QuerydslConfig;
import com.blogitory.blog.follow.entity.Follow;
import com.blogitory.blog.follow.entity.FollowDummy;
import com.blogitory.blog.member.entity.Member;
import com.blogitory.blog.member.repository.MemberRepository;
import jakarta.persistence.EntityManager;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

/**
 * Follow repository test.
 *
 * @author woonseok
 * @since 1.0
 */
@Import({JpaConfig.class, QuerydslConfig.class})
@DataJpaTest
class FollowRepositoryTest {

  @Autowired
  FollowRepository followRepository;

  @Autowired
  MemberRepository memberRepository;

  @Autowired
  EntityManager entityManager;

  @AfterEach
  void teardown() {
    entityManager.createNativeQuery("ALTER TABLE `member` ALTER COLUMN `member_no` RESTART")
            .executeUpdate();
    entityManager.createNativeQuery("ALTER TABLE `follow` ALTER COLUMN `follow_no` RESTART")
            .executeUpdate();
  }

  @Test
  @DisplayName("Follow 저장")
  void followSave() {
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
            null,
            true,
            true,
            true,
            true
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
            null,
            true,
            true,
            true,
            true
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

  @Test
  @DisplayName("팔로잉 카운트")
  void countFollowee() {
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
            null,
            true,
            true,
            true,
            true
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
            null,
            true,
            true,
            true,
            true
    );
    leader = memberRepository.save(leader);

    Follow follow = FollowDummy.dummy(follower, leader);
    followRepository.save(follow);

    Long count = followRepository.countFollowee(leader.getMemberNo());

    assertEquals(1L, count.longValue());
  }

  @Test
  @DisplayName("팔로잉 카운트")
  void countFollower() {
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
            null,
            true,
            true,
            true,
            true
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
            null,
            true,
            true,
            true,
            true
    );
    leader = memberRepository.save(leader);

    Follow follow = FollowDummy.dummy(follower, leader);
    followRepository.save(follow);

    Long count = followRepository.countFollower(follower.getMemberNo());

    assertEquals(1L, count.longValue());
  }

  @Test
  @DisplayName("연관된 팔로우 조회")
  void findRelatedByMemberNo() {
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
            null,
            true,
            true,
            true,
            true
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
            null,
            true,
            true,
            true,
            true
    );
    leader = memberRepository.save(leader);

    Follow follow = FollowDummy.dummy(follower, leader);
    followRepository.save(follow);

    List<Follow> follows = followRepository.findRelatedByMemberNo(follower.getMemberNo());

    assertEquals(follow.getFollowTo().getMemberNo(),
            follows.getFirst().getFollowTo().getMemberNo());
    assertEquals(follow.getFollowFrom().getMemberNo(),
            follows.getFirst().getFollowFrom().getMemberNo());
    assertEquals(follow.getFollowNo(),
            follows.getFirst().getFollowNo());
  }
}