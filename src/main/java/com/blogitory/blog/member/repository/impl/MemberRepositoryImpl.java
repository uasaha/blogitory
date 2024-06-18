package com.blogitory.blog.member.repository.impl;

import com.blogitory.blog.member.dto.MemberMyProfileResponseDto;
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

  /**
   * {@inheritDoc}
   */
  @Override
  public Optional<MemberMyProfileResponseDto> getMyProfile(Integer memberNo) {
    return Optional.ofNullable(
            from(member)
                    .select(Projections.constructor(
                            MemberMyProfileResponseDto.class,
                            member.email,
                            member.name,
                            member.profileThumb,
                            member.introEmail,
                            member.github,
                            member.twitter,
                            member.facebook,
                            member.homepage,
                            member.createdAt))
                    .where(member.memberNo.eq(memberNo))
                    .fetchOne());
  }

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
