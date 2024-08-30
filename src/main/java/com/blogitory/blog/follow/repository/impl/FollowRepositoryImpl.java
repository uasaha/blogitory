package com.blogitory.blog.follow.repository.impl;

import com.blogitory.blog.follow.dto.response.GetFollowResponseDto;
import com.blogitory.blog.follow.entity.Follow;
import com.blogitory.blog.follow.entity.QFollow;
import com.blogitory.blog.follow.repository.FollowRepositoryCustom;
import com.blogitory.blog.member.entity.QMember;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPQLQueryFactory;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

/**
 * Implementation of Follow Repository for querydsl.
 *
 * @author woonseok
 * @since 1.0
 **/
public class FollowRepositoryImpl extends QuerydslRepositorySupport
        implements FollowRepositoryCustom {
  private final JPQLQueryFactory queryFactory;

  QFollow follow = QFollow.follow;
  QMember followTo = new QMember("followTo");
  QMember followFrom = new QMember("followFrom");

  public FollowRepositoryImpl(JPQLQueryFactory queryFactory) {
    super(Follow.class);
    this.queryFactory = queryFactory;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Long countFollowee(Integer followFromNo) {
    QMember member = QMember.member;

    return queryFactory.from(follow)
            .select(follow.followNo)
            .innerJoin(member).on(follow.followFrom.memberNo.eq(member.memberNo))
            .where(follow.followFrom.memberNo.eq(followFromNo))
            .fetchCount();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Long countFollower(Integer followToNo) {
    QMember member = QMember.member;

    return queryFactory.from(follow)
            .select(follow.followNo)
            .innerJoin(member).on(follow.followTo.memberNo.eq(member.memberNo))
            .where(follow.followTo.memberNo.eq(followToNo))
            .fetchCount();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<Follow> findRelatedByMemberNo(Integer memberNo) {
    QMember member = QMember.member;

    List<Follow> followToList = queryFactory.from(follow)
            .select(follow)
            .innerJoin(member).on(follow.followTo.memberNo.eq(member.memberNo))
            .where(follow.followTo.memberNo.eq(memberNo))
            .fetch();

    List<Follow> followFromList = queryFactory.from(follow)
            .select(follow)
            .innerJoin(member).on(follow.followFrom.memberNo.eq(member.memberNo))
            .where(follow.followFrom.memberNo.eq(memberNo))
            .fetch();

    followToList.addAll(followFromList);

    return followToList;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Optional<Follow> findByFromNoAndToUsername(Integer fromNo, String toUsername) {
    return Optional.ofNullable(
            queryFactory.selectFrom(follow)
                    .innerJoin(followTo).on(follow.followTo.memberNo.eq(followTo.memberNo))
                    .innerJoin(followFrom).on(follow.followFrom.memberNo.eq(followFrom.memberNo))
                    .where(followTo.username.eq(toUsername))
                    .where(followFrom.memberNo.eq(fromNo))
                    .fetchFirst());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<GetFollowResponseDto> getAllFollowerByToUsername(String toUsername) {
    return queryFactory
            .from(follow)
            .select(Projections.constructor(
                    GetFollowResponseDto.class,
                    followFrom.profileThumb,
                    followFrom.username,
                    followFrom.name))
            .innerJoin(followTo).on(follow.followTo.memberNo.eq(followTo.memberNo))
            .innerJoin(followFrom).on(follow.followFrom.memberNo.eq(followFrom.memberNo))
            .where(followTo.username.eq(toUsername))
            .fetch();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<GetFollowResponseDto> getAllFollowingByFromUsername(String fromUsername) {
    return queryFactory
            .from(follow)
            .select(Projections.constructor(
                    GetFollowResponseDto.class,
                    followTo.profileThumb,
                    followTo.username,
                    followTo.name))
            .innerJoin(followTo).on(follow.followTo.memberNo.eq(followTo.memberNo))
            .innerJoin(followFrom).on(follow.followFrom.memberNo.eq(followFrom.memberNo))
            .where(followFrom.username.eq(fromUsername))
            .fetch();
  }
}
