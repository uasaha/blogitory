package com.blogitory.blog.follow.repository.impl;

import com.blogitory.blog.follow.entity.Follow;
import com.blogitory.blog.follow.entity.QFollow;
import com.blogitory.blog.follow.repository.FollowRepositoryCustom;
import com.blogitory.blog.member.entity.QMember;
import java.util.List;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

/**
 * Implementation of Follow Repository for querydsl.
 *
 * @author woonseok
 * @since 1.0
 **/
public class FollowRepositoryImpl extends QuerydslRepositorySupport
        implements FollowRepositoryCustom {

  QFollow follow = QFollow.follow;
  QMember member = QMember.member;

  public FollowRepositoryImpl() {
    super(Follow.class);
  }

  @Override
  public Long countFollowee(Integer followFromNo) {
    return from(follow)
            .select(follow.followNo)
            .innerJoin(member).on(follow.followFrom.memberNo.eq(member.memberNo))
            .where(follow.followFrom.memberNo.eq(followFromNo))
            .fetchCount();
  }

  @Override
  public Long countFollower(Integer followToNo) {
    return from(follow)
            .select(follow.followNo)
            .innerJoin(member).on(follow.followTo.memberNo.eq(member.memberNo))
            .where(follow.followTo.memberNo.eq(followToNo))
            .fetchCount();
  }

  @Override
  public List<Follow> findRelatedByMemberNo(Integer memberNo) {
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
}
