package com.blogitory.blog.visitant.repository.impl;

import com.blogitory.blog.blog.entity.QBlog;
import com.blogitory.blog.visitant.entity.QVisitant;
import com.blogitory.blog.visitant.entity.Visitant;
import com.blogitory.blog.visitant.repository.VisitantRepositoryCustom;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.LocalDate;
import java.util.Optional;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

/**
 * Implementation of visitant repository for using querydsl.
 *
 * @author woonseok
 * @Date 2024-09-10
 * @since 1.0
 **/
public class VisitantRepositoryImpl extends QuerydslRepositorySupport implements VisitantRepositoryCustom {

  private final JPAQueryFactory queryFactory;

  public VisitantRepositoryImpl(JPAQueryFactory queryFactory) {
    super(Visitant.class);
    this.queryFactory = queryFactory;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Integer getCountByBlogUrl(String blogUrl) {
    QVisitant visitant = QVisitant.visitant;
    QBlog blog = QBlog.blog;

    return queryFactory.from(visitant)
            .select(visitant.visitantCnt.sum())
            .innerJoin(blog).on(visitant.blog.blogNo.eq(blog.blogNo))
            .where(blog.urlName.eq(blogUrl))
            .fetchOne();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Integer getCountByBlogUrlAndDate(String blogUrl, LocalDate date) {
    QVisitant visitant = QVisitant.visitant;
    QBlog blog = QBlog.blog;

    return queryFactory.from(visitant)
            .select(visitant.visitantCnt.sum())
            .innerJoin(blog).on(visitant.blog.blogNo.eq(blog.blogNo))
            .where(visitant.visitDate.eq(date))
            .where(blog.urlName.eq(blogUrl))
            .fetchOne();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Optional<Visitant> findByBlogUrlAndDate(String blogUrl, LocalDate date) {
    QVisitant visitant = QVisitant.visitant;
    QBlog blog = QBlog.blog;

    return Optional.ofNullable(
            queryFactory.selectFrom(visitant)
                    .innerJoin(blog).on(visitant.blog.blogNo.eq(blog.blogNo))
                    .where(visitant.visitDate.eq(date))
                    .where(blog.urlName.eq(blogUrl))
                    .fetchOne());
  }
}