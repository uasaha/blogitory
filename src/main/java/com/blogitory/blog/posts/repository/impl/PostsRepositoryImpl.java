package com.blogitory.blog.posts.repository.impl;

import com.blogitory.blog.blog.entity.QBlog;
import com.blogitory.blog.category.entity.QCategory;
import com.blogitory.blog.member.entity.QMember;
import com.blogitory.blog.posts.dto.response.GetPostResponseDto;
import com.blogitory.blog.posts.entity.Posts;
import com.blogitory.blog.posts.entity.QPosts;
import com.blogitory.blog.posts.repository.PostsRepositoryCustom;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.Optional;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

/**
 * Post repository implementation.
 *
 * @author woonseok
 * @Date 2024-08-03
 * @since 1.0
 **/
public class PostsRepositoryImpl extends QuerydslRepositorySupport
        implements PostsRepositoryCustom {
  private final JPAQueryFactory queryFactory;

  public PostsRepositoryImpl(JPAQueryFactory jpaQueryFactory) {
    super(Posts.class);
    this.queryFactory = jpaQueryFactory;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Optional<GetPostResponseDto> getPostByPostUrl(String url) {
    QPosts posts = QPosts.posts;
    QMember member = QMember.member;
    QCategory category = QCategory.category;
    QBlog blog = QBlog.blog;

    return Optional.ofNullable(
            queryFactory.from(posts)
                    .select(Projections.constructor(
                            GetPostResponseDto.class,
                            member.username,
                            member.name,
                            blog.name,
                            blog.urlName,
                            category.categoryNo,
                            category.name,
                            posts.url,
                            posts.subject,
                            posts.summary,
                            posts.detail,
                            posts.views,
                            posts.createdAt,
                            posts.updatedAt))
                    .innerJoin(category).on(category.categoryNo.eq(posts.category.categoryNo))
                    .innerJoin(blog).on(blog.blogNo.eq(category.blog.blogNo))
                    .innerJoin(member).on(member.memberNo.eq(blog.member.memberNo))
                    .where(posts.url.eq(url).and(posts.deleted.isFalse()))
                    .where(category.deleted.isFalse())
                    .where(blog.deleted.isFalse())
                    .where(member.blocked.isFalse().and(member.left.isFalse()))
                    .fetchOne());
  }
}
