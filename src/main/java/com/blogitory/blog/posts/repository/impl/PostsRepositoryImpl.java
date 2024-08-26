package com.blogitory.blog.posts.repository.impl;

import com.blogitory.blog.blog.entity.QBlog;
import com.blogitory.blog.category.entity.QCategory;
import com.blogitory.blog.comment.entity.QComment;
import com.blogitory.blog.heart.entity.QHeart;
import com.blogitory.blog.member.entity.QMember;
import com.blogitory.blog.posts.dto.response.GetPostForModifyResponseDto;
import com.blogitory.blog.posts.dto.response.GetPostResponseDto;
import com.blogitory.blog.posts.dto.response.GetRecentPostResponseDto;
import com.blogitory.blog.posts.entity.Posts;
import com.blogitory.blog.posts.entity.QPosts;
import com.blogitory.blog.posts.repository.PostsRepositoryCustom;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
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
  private final QMember member;
  private final QCategory category;
  private final QBlog blog;
  private final QPosts posts;
  private final QComment comment;
  private final QHeart heart;

  /**
   * Constructor.
   *
   * @param jpaQueryFactory jpaQueryFactory
   */
  public PostsRepositoryImpl(JPAQueryFactory jpaQueryFactory) {
    super(Posts.class);
    member = QMember.member;
    category = QCategory.category;
    blog = QBlog.blog;
    posts = QPosts.posts;
    comment = QComment.comment;
    heart = QHeart.heart;
    this.queryFactory = jpaQueryFactory;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Optional<GetPostResponseDto> getPostByPostUrl(String url) {
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
                            posts.thumbnail,
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

  /**
   * {@inheritDoc}
   */
  @Override
  public Optional<GetPostForModifyResponseDto> getPostForModifyByUrl(
          Integer memberNo, String postUrl) {
    return Optional.ofNullable(
            from(posts)
                    .select(Projections.fields(
                            GetPostForModifyResponseDto.class,
                            blog.name.as("blogName"),
                            category.name.as("categoryName"),
                            posts.subject.as("title"),
                            posts.url.as("postUrl"),
                            posts.thumbnail.as("thumbnailUrl"),
                            posts.summary.as("summary"),
                            posts.detail.as("detail")))
                    .innerJoin(category).on(posts.category.categoryNo.eq(category.categoryNo))
                    .innerJoin(blog).on(category.blog.blogNo.eq(blog.blogNo))
                    .innerJoin(member).on(blog.member.memberNo.eq(member.memberNo))
                    .where(posts.url.eq(postUrl))
                    .where(posts.deleted.isFalse())
                    .where(category.deleted.isFalse())
                    .where(blog.deleted.isFalse())
                    .where(member.memberNo.eq(memberNo))
                    .where(member.blocked.isFalse().and(member.left.isFalse()))
                    .fetchOne());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Page<GetRecentPostResponseDto> getRecentPosts(Pageable pageable) {
    List<GetRecentPostResponseDto> postsList = recentPosts(pageable, Expressions.TRUE);

    long total = from(posts)
            .select(posts.postsNo)
            .innerJoin(category).on(posts.category.categoryNo.eq(category.categoryNo))
            .innerJoin(blog).on(category.blog.blogNo.eq(blog.blogNo))
            .innerJoin(member).on(blog.member.memberNo.eq(member.memberNo))
            .where(member.left.isFalse().and(member.blocked.isFalse()))
            .where(blog.deleted.isFalse())
            .where(category.deleted.isFalse())
            .where(posts.deleted.isFalse())
            .fetchCount();

    return new PageImpl<>(postsList, pageable, total);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<GetRecentPostResponseDto> getRecentPostByUsername(
          Pageable pageable, String username) {

    return recentPosts(pageable, member.username.eq(username));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<GetRecentPostResponseDto> getRecentPostByBlog(
          Pageable pageable, String blogUrl) {

    return recentPosts(pageable, blog.urlName.eq(blogUrl));
  }

  /**
   * Get recent posts.
   *
   * @param pageable   pageable
   * @param expression boolean expression(condition)
   * @return recent posts
   */
  private List<GetRecentPostResponseDto> recentPosts(Pageable pageable,
                                                     BooleanExpression expression) {
    return from(posts)
            .select(Projections.constructor(
                    GetRecentPostResponseDto.class,
                    blog.urlName,
                    blog.name,
                    member.username,
                    blog.background,
                    posts.url,
                    posts.subject,
                    posts.summary,
                    posts.thumbnail,
                    posts.createdAt,
                    JPAExpressions.select(heart.count())
                            .from(heart)
                            .where(heart.posts.postsNo.eq(posts.postsNo))
                            .where(heart.deleted.isFalse()),
                    JPAExpressions.select(comment.count())
                            .from(comment)
                            .where(comment.posts.postsNo.eq(posts.postsNo))
                            .where(comment.deleted.isFalse())))
            .innerJoin(category).on(posts.category.categoryNo.eq(category.categoryNo))
            .innerJoin(blog).on(category.blog.blogNo.eq(blog.blogNo))
            .innerJoin(member).on(blog.member.memberNo.eq(member.memberNo))
            .where(expression)
            .where(member.left.isFalse().and(member.blocked.isFalse()))
            .where(blog.deleted.isFalse())
            .where(category.deleted.isFalse())
            .where(posts.deleted.isFalse())
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .orderBy(posts.createdAt.desc())
            .fetch();
  }
}
