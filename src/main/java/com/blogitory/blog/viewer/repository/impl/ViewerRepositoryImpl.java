package com.blogitory.blog.viewer.repository.impl;

import com.blogitory.blog.posts.entity.QPosts;
import com.blogitory.blog.viewer.entity.QViewer;
import com.blogitory.blog.viewer.entity.Viewer;
import com.blogitory.blog.viewer.repository.ViewerRepositoryCustom;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.LocalDate;
import java.util.Optional;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

/**
 * Implementation of Viewer repository for using querydsl.
 *
 * @author woonseok
 * @Date 2024-09-09
 * @since 1.0
 **/
public class ViewerRepositoryImpl extends QuerydslRepositorySupport
        implements ViewerRepositoryCustom {

  private final JPAQueryFactory queryFactory;

  public ViewerRepositoryImpl(JPAQueryFactory queryFactory) {
    super(Viewer.class);
    this.queryFactory = queryFactory;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Integer getCountByPostsUrl(String postsUrl) {
    QViewer viewer = QViewer.viewer;
    QPosts posts = QPosts.posts;

    return queryFactory.from(viewer)
            .select(viewer.viewerCnt.sum())
            .innerJoin(posts).on(viewer.posts.postsNo.eq(posts.postsNo))
            .where(posts.url.eq(postsUrl))
            .fetchOne();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Optional<Viewer> findByPostsUrlAndDate(String postsUrl, LocalDate date) {
    QViewer viewer = QViewer.viewer;
    QPosts posts = QPosts.posts;

    return Optional.ofNullable(
            queryFactory.selectFrom(viewer)
            .innerJoin(posts).on(viewer.posts.postsNo.eq(posts.postsNo))
            .where(viewer.viewerDate.eq(date))
            .where(posts.url.eq(postsUrl))
            .fetchOne());
  }
}
