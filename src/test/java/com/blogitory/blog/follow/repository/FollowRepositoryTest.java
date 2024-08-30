package com.blogitory.blog.follow.repository;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.blogitory.blog.commons.config.JpaConfig;
import com.blogitory.blog.commons.config.QuerydslConfig;
import com.blogitory.blog.follow.dto.response.GetFollowResponseDto;
import com.blogitory.blog.follow.entity.Follow;
import com.blogitory.blog.follow.entity.FollowDummy;
import com.blogitory.blog.member.entity.Member;
import com.blogitory.blog.member.entity.MemberDummy;
import com.blogitory.blog.member.repository.MemberRepository;
import jakarta.persistence.EntityManager;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.util.ReflectionTestUtils;

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

  @Test
  @DisplayName("팔로우 한 사람 조회")
  void findByFollowToUsernameAndFollowFromNo() {
    Member followTo = MemberDummy.dummy();
    memberRepository.save(followTo);

    Member followFrom = MemberDummy.dummy();
    ReflectionTestUtils.setField(followFrom, "memberNo", 2);
    ReflectionTestUtils.setField(followFrom, "username", "followFrom");
    memberRepository.save(followFrom);

    Follow follow = FollowDummy.dummy(followTo, followFrom);
    followRepository.save(follow);

    Optional<Follow> result = followRepository
            .findByFromNoAndToUsername(followFrom.getMemberNo(), followTo.getUsername());

    assertTrue(result.isPresent());

    Follow resultEntity = result.get();
    assertEquals(resultEntity.getFollowTo().getMemberNo(), followTo.getMemberNo());
  }

  @Test
  @DisplayName("팔로워 전체 조회")
  void getAllFollowerByToUsername() {
    Member followTo = MemberDummy.dummy();
    memberRepository.save(followTo);
    Member followFrom = MemberDummy.dummy();
    ReflectionTestUtils.setField(followFrom, "memberNo", 2);
    ReflectionTestUtils.setField(followFrom, "username", "followFrom");
    memberRepository.save(followFrom);
    Follow follow = FollowDummy.dummy(followTo, followFrom);
    followRepository.save(follow);

    List<GetFollowResponseDto> resultList = followRepository.getAllFollowerByToUsername(followTo.getUsername());

    assertFalse(resultList.isEmpty());

    GetFollowResponseDto result = resultList.getFirst();

    assertEquals(followFrom.getUsername(), result.getUsername());
    assertEquals(followFrom.getName(), result.getName());
  }

  @Test
  @DisplayName("팔로워 전체 조회")
  void getAllFollowingByFromUsername() {
    Member followTo = MemberDummy.dummy();
    memberRepository.save(followTo);
    Member followFrom = MemberDummy.dummy();
    ReflectionTestUtils.setField(followFrom, "memberNo", 2);
    ReflectionTestUtils.setField(followFrom, "username", "followFrom");
    memberRepository.save(followFrom);
    Follow follow = FollowDummy.dummy(followTo, followFrom);
    followRepository.save(follow);

    List<GetFollowResponseDto> resultList = followRepository.getAllFollowingByFromUsername(followFrom.getUsername());

    assertFalse(resultList.isEmpty());

    GetFollowResponseDto result = resultList.getFirst();

    assertEquals(followTo.getUsername(), result.getUsername());
    assertEquals(followTo.getName(), result.getName());
  }
}