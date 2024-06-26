package com.blogitory.blog.member.repository.impl;

import com.blogitory.blog.follow.entity.QFollow;
import com.blogitory.blog.member.dto.MemberPersistInfoDto;
import com.blogitory.blog.member.entity.Member;
import com.blogitory.blog.member.entity.QMember;
import com.blogitory.blog.member.repository.MemberRepositoryCustom;
import com.querydsl.core.types.Projections;
import java.util.Optional;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

/**
 * Member Repository for using Querydsl.
 *
 * @author woonseok
 * @since 1.0
 **/
public class MemberRepositoryImpl extends QuerydslRepositorySupport
        implements MemberRepositoryCustom {
  public MemberRepositoryImpl() {
    super(Member.class);
  }

  QMember member = QMember.member;
  QFollow follow = QFollow.follow;

  /**
   * {@inheritDoc}
   */
  @Override
  public Optional<MemberPersistInfoDto> getPersistInfo(Integer memberNo) {
    return Optional.ofNullable(
            from(member)
                    .select(Projections.constructor(
                            MemberPersistInfoDto.class,
                            member.username,
                            member.name,
                            member.profileThumb
                    ))
                    .where(member.memberNo.eq(memberNo))
                    .fetchFirst());
  }
}
