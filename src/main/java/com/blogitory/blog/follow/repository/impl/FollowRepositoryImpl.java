package com.blogitory.blog.follow.repository.impl;

import com.blogitory.blog.follow.entity.Follow;
import com.blogitory.blog.follow.entity.QFollow;
import com.blogitory.blog.follow.repository.FollowRepositoryCustom;
import com.blogitory.blog.member.entity.QMember;
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

  public FollowRepositoryImpl(JPQLQueryFactory queryFactory) {
    super(Follow.class);
    this.queryFactory = queryFactory;
  }

  @Override
  public Long countFollowee(Integer followFromNo) {
    QFollow follow = QFollow.follow;
    QMember member = QMember.member;

    return from(follow)
            .select(follow.followNo)
            .innerJoin(member).on(follow.followFrom.memberNo.eq(member.memberNo))
            .where(follow.followFrom.memberNo.eq(followFromNo))
            .fetchCount();
  }

  @Override
  public Long countFollower(Integer followToNo) {
    QFollow follow = QFollow.follow;
    QMember member = QMember.member;

    return from(follow)
            .select(follow.followNo)
            .innerJoin(member).on(follow.followTo.memberNo.eq(member.memberNo))
            .where(follow.followTo.memberNo.eq(followToNo))
            .fetchCount();
  }

  @Override
  public List<Follow> findRelatedByMemberNo(Integer memberNo) {
    QFollow follow = QFollow.follow;
    QMember member = QMember.member;

    List<Follow> followTo = from(follow)
            .select(follow)
            .innerJoin(member).on(follow.followTo.memberNo.eq(member.memberNo))
            .where(follow.followTo.memberNo.eq(memberNo))
            .fetch();

    List<Follow> followFrom = from(follow)
            .select(follow)
            .innerJoin(member).on(follow.followFrom.memberNo.eq(member.memberNo))
            .where(follow.followFrom.memberNo.eq(memberNo))
            .fetch();

    followTo.addAll(followFrom);

    return followTo;
  }

  @Override
  public Optional<Follow> findByFromNoAndToUsername(Integer fromNo, String toUsername) {
    QFollow follow = QFollow.follow;
    QMember followTo = QMember.member;
    QMember followFrom = new QMember("followFrom");

    return Optional.ofNullable(
            queryFactory.selectFrom(follow)
                    .innerJoin(followTo).on(follow.followTo.memberNo.eq(followTo.memberNo))
                    .innerJoin(followFrom).on(follow.followFrom.memberNo.eq(followFrom.memberNo))
                    .where(followTo.username.eq(toUsername))
                    .where(followFrom.memberNo.eq(fromNo))
                    .fetchFirst());
  }
}
