package com.blogitory.blog.follow.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.blogitory.blog.follow.dto.response.GetAllFollowResponseDto;
import com.blogitory.blog.follow.dto.response.GetFollowResponseDto;
import com.blogitory.blog.follow.entity.Follow;
import com.blogitory.blog.follow.entity.FollowDummy;
import com.blogitory.blog.follow.repository.FollowRepository;
import com.blogitory.blog.follow.service.impl.FollowServiceImpl;
import com.blogitory.blog.member.entity.Member;
import com.blogitory.blog.member.entity.MemberDummy;
import com.blogitory.blog.member.repository.MemberRepository;
import com.blogitory.blog.notice.service.NoticeService;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.test.util.ReflectionTestUtils;

/**
 * Follow service test.
 *
 * @author woonseok
 * @Date 2024-08-26
 * @since 1.0
 **/
class FollowServiceTest {
  ApplicationEventPublisher eventPublisher;
  MemberRepository memberRepository;
  FollowRepository followRepository;
  NoticeService noticeService;
  FollowService followService;

  @BeforeEach
  void setUp() {
    memberRepository = mock(MemberRepository.class);
    followRepository = mock(FollowRepository.class);
    noticeService = mock(NoticeService.class);
    eventPublisher = mock(ApplicationEventPublisher.class);

    followService = new FollowServiceImpl(eventPublisher, memberRepository, followRepository);
  }

  @Test
  @DisplayName("팔로우")
  void follow() {
    Member followTo = MemberDummy.dummy();
    Member followFrom = MemberDummy.dummy();
    ReflectionTestUtils.setField(followFrom, "memberNo", 2);
    ReflectionTestUtils.setField(followFrom, "username", "followFrom");
    Follow follow = FollowDummy.dummy(followTo, followFrom);

    when(memberRepository.findById(anyInt())).thenReturn(Optional.of(followTo));
    when(memberRepository.findByUsername(anyString())).thenReturn(Optional.of(followFrom));
    when(followRepository.save(any())).thenReturn(follow);

    followService.follow(followTo.getMemberNo(), followFrom.getUsername());

    verify(followRepository, times(1)).save(any(Follow.class));
  }

  @Test
  @DisplayName("언팔로우")
  void unFollow() {
    Member followTo = MemberDummy.dummy();
    Member followFrom = MemberDummy.dummy();
    ReflectionTestUtils.setField(followFrom, "memberNo", 2);
    ReflectionTestUtils.setField(followFrom, "username", "followFrom");
    Follow follow = FollowDummy.dummy(followTo, followFrom);

    when(followRepository.findByFromNoAndToUsername(
            followFrom.getMemberNo(), followTo.getUsername()))
            .thenReturn(Optional.of(follow));

    followService.unFollow(followFrom.getMemberNo(), followTo.getUsername());

    verify(followRepository, times(1)).delete(any(Follow.class));
  }

  @Test
  @DisplayName("팔로우 여부")
  void isFollowed() {
    Member followTo = MemberDummy.dummy();
    Member followFrom = MemberDummy.dummy();
    ReflectionTestUtils.setField(followFrom, "memberNo", 2);
    ReflectionTestUtils.setField(followFrom, "username", "followFrom");
    Follow follow = FollowDummy.dummy(followTo, followFrom);

    when(followRepository.findByFromNoAndToUsername(anyInt(), anyString()))
            .thenReturn(Optional.of(follow));

    boolean isFollowed = followService.isFollowed(followFrom.getMemberNo(), followTo.getUsername());

    assertTrue(isFollowed);
  }

  @Test
  @DisplayName("팔로우 전체 조회")
  void getAllFollowInfo() {
    Member followTo = MemberDummy.dummy();

    Member followFrom = MemberDummy.dummy();
    ReflectionTestUtils.setField(followFrom, "memberNo", 2);
    ReflectionTestUtils.setField(followFrom, "username", "followFrom");

    GetFollowResponseDto followResponseDto =
            new GetFollowResponseDto(followTo.getProfileThumb(),
                    followTo.getUsername(), followTo.getName());

    GetFollowResponseDto followingResponseDto =
            new GetFollowResponseDto(followFrom.getProfileThumb(),
                    followFrom.getUsername(), followFrom.getName());

    when(followRepository.getAllFollowerByToUsername(anyString()))
            .thenReturn(List.of(followingResponseDto));
    when(followRepository.getAllFollowingByFromUsername(anyString()))
            .thenReturn(List.of(followResponseDto));
    when(memberRepository.findById(anyInt())).thenReturn(Optional.of(followTo));

    GetAllFollowResponseDto responseDto =
            followService.getAllFollowInfo(followTo.getMemberNo(), followTo.getUsername());

    assertTrue(responseDto.isMine());
    assertEquals(followResponseDto.getUsername(), responseDto.getFollowings().getFirst().getUsername());
    assertEquals(followingResponseDto.getUsername(), responseDto.getFollowers().getFirst().getUsername());
  }

  @Test
  @DisplayName("팔로우 전체 조회 - member null")
  void getAllFollowInfoMemberIsNull() {
    Member followTo = MemberDummy.dummy();

    Member followFrom = MemberDummy.dummy();
    ReflectionTestUtils.setField(followFrom, "memberNo", 2);
    ReflectionTestUtils.setField(followFrom, "username", "followFrom");

    GetFollowResponseDto followResponseDto =
            new GetFollowResponseDto(followTo.getProfileThumb(),
                    followTo.getUsername(), followTo.getName());

    GetFollowResponseDto followingResponseDto =
            new GetFollowResponseDto(followFrom.getProfileThumb(),
                    followFrom.getUsername(), followFrom.getName());

    when(followRepository.getAllFollowerByToUsername(anyString()))
            .thenReturn(List.of(followingResponseDto));
    when(followRepository.getAllFollowingByFromUsername(anyString()))
            .thenReturn(List.of(followResponseDto));
    when(memberRepository.findById(anyInt())).thenReturn(Optional.empty());

    GetAllFollowResponseDto responseDto =
            followService.getAllFollowInfo(followTo.getMemberNo(), followTo.getUsername());

    assertFalse(responseDto.isMine());
    assertEquals(followResponseDto.getUsername(), responseDto.getFollowings().getFirst().getUsername());
    assertEquals(followingResponseDto.getUsername(), responseDto.getFollowers().getFirst().getUsername());
  }
}