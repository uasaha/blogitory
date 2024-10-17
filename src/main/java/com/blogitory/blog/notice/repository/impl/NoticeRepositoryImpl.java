package com.blogitory.blog.notice.repository.impl;

import com.blogitory.blog.member.entity.QMember;
import com.blogitory.blog.notice.entity.Notice;
import com.blogitory.blog.notice.entity.QNotice;
import com.blogitory.blog.notice.repository.NoticeRepositoryCustom;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import java.util.Objects;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

/**
 * Implementation of Notice repository.
 *
 * @author woonseok
 * @since 1.0
 **/
public class NoticeRepositoryImpl extends QuerydslRepositorySupport
        implements NoticeRepositoryCustom {

  private final JPAQueryFactory queryFactory;

  public NoticeRepositoryImpl(JPAQueryFactory queryFactory) {
    super(Notice.class);
    this.queryFactory = queryFactory;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<Notice> findLostNoticesByMemberNo(Integer memberNo, Long lastNoticeNo) {
    QNotice notice = QNotice.notice;
    QMember member = QMember.member;

    return queryFactory
            .selectFrom(notice)
            .innerJoin(notice.member, member)
            .where(member.memberNo.eq(memberNo))
            .where(notice.read.isFalse())
            .where(notice.noticeNo.gt(lastNoticeNo))
            .fetch();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Page<Notice> findAllNoticeByMemberNo(Pageable pageable, Integer memberNo) {
    QNotice notice = QNotice.notice;
    QMember member = QMember.member;

    List<Notice> content = queryFactory
            .selectFrom(notice)
            .innerJoin(notice.member, member)
            .where(member.memberNo.eq(memberNo))
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .orderBy(notice.createdAt.desc())
            .fetch();

    Long total = queryFactory
            .from(notice)
            .select(notice.count())
            .innerJoin(notice.member, member)
            .where(member.memberNo.eq(memberNo))
            .fetchOne();

    return new PageImpl<>(content, pageable, Objects.isNull(total) ? 0 : total);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean hasNonReadNotice(Integer memberNo) {
    QNotice notice = QNotice.notice;
    QMember member = QMember.member;

    Long count = queryFactory
            .from(notice)
            .select(notice.count())
            .innerJoin(notice.member, member)
            .where(member.memberNo.eq(memberNo))
            .where(notice.read.isFalse())
            .fetchOne();

    return (Objects.isNull(count) ? 0 : count) > 0;
  }
}
