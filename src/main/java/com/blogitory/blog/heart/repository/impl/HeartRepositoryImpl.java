package com.blogitory.blog.heart.repository.impl;

import com.blogitory.blog.heart.entity.Heart;
import com.blogitory.blog.heart.entity.QHeart;
import com.blogitory.blog.heart.repository.HeartRepositoryCustom;
import com.blogitory.blog.member.entity.QMember;
import com.blogitory.blog.posts.entity.QPosts;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.Optional;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

/**
 * Implementation of HeartRepository.
 *
 * @author woonseok
 * @Date 2024-08-29
 * @since 1.0
 **/
public class HeartRepositoryImpl extends QuerydslRepositorySupport
        implements HeartRepositoryCustom {

  private final JPAQueryFactory queryFactory;

  public HeartRepositoryImpl(JPAQueryFactory jpaQueryFactory) {
    super(Heart.class);
    this.queryFactory = jpaQueryFactory;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Optional<Heart> findByMemberNoAndPostsUrl(Integer memberNo, String postsUrl) {
    QHeart heart = QHeart.heart;
    QMember member = QMember.member;
    QPosts posts = QPosts.posts;

    return Optional.ofNullable(queryFactory
            .selectFrom(heart)
            .innerJoin(heart.member, member)
            .innerJoin(heart.posts, posts)
            .where(member.memberNo.eq(memberNo))
            .where(member.blocked.isFalse().and(member.left.isFalse()))
            .where(posts.url.eq(postsUrl))
            .where(posts.deleted.isFalse().and(posts.open.isTrue()))
            .fetchOne());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Long getHeartCountsByPost(String postsUrl) {
    QHeart heart = QHeart.heart;
    QPosts posts = QPosts.posts;

    return queryFactory
            .from(heart)
            .select(heart.count())
            .innerJoin(posts).on(posts.postsNo.eq(heart.posts.postsNo))
            .where(posts.url.eq(postsUrl))
            .where(heart.deleted.isFalse())
            .where(posts.deleted.isFalse().and(posts.open.isTrue()))
            .fetchOne();
  }
}
