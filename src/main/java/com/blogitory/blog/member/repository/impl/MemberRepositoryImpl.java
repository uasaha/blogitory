package com.blogitory.blog.member.repository.impl;

import com.blogitory.blog.member.dto.response.GetMemberPersistInfoDto;
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
  public Optional<GetMemberPersistInfoDto> getPersistInfo(Integer memberNo) {
    return Optional.ofNullable(
            from(member)
                    .select(Projections.constructor(
                            GetMemberPersistInfoDto.class,
                            member.username,
                            member.name,
                            member.profileThumb
                    ))
                    .where(member.memberNo.eq(memberNo))
                    .fetchFirst());
  }
}
