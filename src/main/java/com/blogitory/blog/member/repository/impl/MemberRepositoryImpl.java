package com.blogitory.blog.member.repository.impl;

import com.blogitory.blog.member.dto.response.GetMemberPersistInfoDto;
import com.blogitory.blog.member.entity.Member;
import com.blogitory.blog.member.entity.QMember;
import com.blogitory.blog.member.repository.MemberRepositoryCustom;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
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

  private final JPAQueryFactory queryFactory;

  public MemberRepositoryImpl(JPAQueryFactory queryFactory) {
    super(Member.class);
    this.queryFactory = queryFactory;
  }

  QMember member = QMember.member;

  /**
   * {@inheritDoc}
   */
  @Override
  public Optional<GetMemberPersistInfoDto> getPersistInfo(Integer memberNo) {
    return Optional.ofNullable(
            queryFactory.from(member)
                    .select(Projections.constructor(
                            GetMemberPersistInfoDto.class,
                            member.username,
                            member.name,
                            member.profileThumb
                    ))
                    .where(member.memberNo.eq(memberNo))
                    .fetchFirst());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Optional<Member> findByOauthId(String provider, String id) {
    return Optional.ofNullable(
            queryFactory.selectFrom(member)
                    .where(member.oauth.eq(provider).and(member.email.eq(id)))
                    .fetchFirst());
  }
}
