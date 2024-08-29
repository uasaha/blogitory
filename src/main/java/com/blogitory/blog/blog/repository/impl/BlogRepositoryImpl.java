package com.blogitory.blog.blog.repository.impl;

import com.blogitory.blog.blog.dto.response.GetBlogInHeaderResponseDto;
import com.blogitory.blog.blog.dto.response.GetBlogInSettingsResponseDto;
import com.blogitory.blog.blog.dto.response.GetBlogResponseDto;
import com.blogitory.blog.blog.dto.response.GetBlogWithCategoryResponseDto;
import com.blogitory.blog.blog.entity.Blog;
import com.blogitory.blog.blog.entity.QBlog;
import com.blogitory.blog.blog.repository.BlogRepositoryCustom;
import com.blogitory.blog.category.dto.GetCategoryInSettingsResponseDto;
import com.blogitory.blog.category.entity.QCategory;
import com.blogitory.blog.image.entity.QImage;
import com.blogitory.blog.member.entity.QMember;
import com.blogitory.blog.posts.entity.QPosts;
import com.querydsl.core.group.GroupBy;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

/**
 * Blog repository implementation.
 *
 * @author woonseok
 * @since 1.0
 **/
public class BlogRepositoryImpl extends QuerydslRepositorySupport implements BlogRepositoryCustom {
  private final JPAQueryFactory queryFactory;

  public BlogRepositoryImpl(JPAQueryFactory queryFactory) {
    super(Blog.class);
    this.queryFactory = queryFactory;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<GetBlogInSettingsResponseDto> getBlogListByMemberNo(Integer memberNo) {
    QBlog blog = QBlog.blog;
    QMember member = QMember.member;
    QImage image = QImage.image;
    QCategory category = QCategory.category;

    return queryFactory.from(blog)
            .select(blog)
            .innerJoin(blog.member, member)
            .leftJoin(image).on(image.blog.blogNo.eq(blog.blogNo))
            .leftJoin(category).on(blog.blogNo.eq(category.blog.blogNo))
            .where(member.blocked.isFalse().and(member.left.isFalse()))
            .where(blog.member.memberNo.eq(memberNo))
            .where(blog.deleted.isFalse())
            .orderBy(blog.blogNo.asc())
            .transform(
                    GroupBy.groupBy(blog.blogNo)
                            .list(Projections.constructor(GetBlogInSettingsResponseDto.class,
                                    blog.name,
                                    blog.bio,
                                    blog.urlName,
                                    blog.createdAt,
                                    image.url,
                                    image.originName,
                                    GroupBy.list(Projections.constructor(
                                            GetCategoryInSettingsResponseDto.class,
                                            category.categoryNo,
                                            category.name,
                                            category.deleted
                                    ))))
            );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<GetBlogInHeaderResponseDto> getBlogListInHeaderByUsername(String username) {
    QBlog blog = QBlog.blog;
    QMember member = QMember.member;

    return queryFactory.from(blog)
            .select(Projections.constructor(
                    GetBlogInHeaderResponseDto.class,
                    blog.name,
                    blog.urlName))
            .innerJoin(member).on(blog.member.memberNo.eq(member.memberNo))
            .where(member.username.eq(username)
                    .and(member.blocked.isFalse())
                    .and(member.left.isFalse())
                    .and(blog.deleted.isFalse()))
            .orderBy(blog.blogNo.asc())
            .fetch();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Optional<GetBlogResponseDto> getBlogByUrl(String url) {
    QBlog blog = QBlog.blog;
    QMember member = QMember.member;
    QImage image = QImage.image;
    QPosts posts = QPosts.posts;
    QCategory category = QCategory.category;

    return Optional.ofNullable(queryFactory
            .from(blog)
            .select(Projections.constructor(
                    GetBlogResponseDto.class,
                    image.url,
                    image.originName,
                    blog.urlName,
                    blog.name,
                    member.name,
                    member.username,
                    blog.bio,
                    ExpressionUtils.as(
                            JPAExpressions.select(posts.count())
                                    .from(posts)
                                    .innerJoin(category)
                                    .on(category.categoryNo.eq(posts.category.categoryNo))
                                    .innerJoin(blog)
                                    .on(blog.blogNo.eq(category.blog.blogNo))
                                    .where(blog.urlName.eq(url)
                                            .and(blog.deleted.isFalse())
                                            .and(category.deleted.isFalse())
                                            .and(posts.deleted.isFalse())),
                            "postsCnt")
                    ))
            .innerJoin(blog.member, member)
            .leftJoin(image).on(image.blog.blogNo.eq(blog.blogNo))
            .where(blog.urlName.eq(url)
                    .and(blog.deleted.isFalse()))
            .fetchOne());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<GetBlogWithCategoryResponseDto> getBlogWithCategoryList(Integer memberNo) {
    QBlog blog = QBlog.blog;
    QMember member = QMember.member;
    QCategory category = QCategory.category;

    return queryFactory.from(blog)
            .select(blog)
            .innerJoin(blog.member, member)
            .leftJoin(category).on(blog.blogNo.eq(category.blog.blogNo))
            .where(blog.member.memberNo.eq(memberNo)
                    .and(blog.deleted.isFalse()))
            .orderBy(blog.blogNo.asc())
            .transform(
                    GroupBy.groupBy(blog.blogNo)
                            .list(Projections.constructor(GetBlogWithCategoryResponseDto.class,
                                    blog.blogNo,
                                    blog.name,
                                    GroupBy.list(Projections.constructor(
                                            GetCategoryInSettingsResponseDto.class,
                                            category.categoryNo,
                                            category.name,
                                            category.deleted
                                    )))));
  }
}
