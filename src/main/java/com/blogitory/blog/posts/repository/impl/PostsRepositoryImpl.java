package com.blogitory.blog.posts.repository.impl;

import com.blogitory.blog.blog.entity.QBlog;
import com.blogitory.blog.category.entity.QCategory;
import com.blogitory.blog.comment.entity.QComment;
import com.blogitory.blog.heart.entity.QHeart;
import com.blogitory.blog.member.entity.QMember;
import com.blogitory.blog.posts.dto.response.GetPopularPostResponseDto;
import com.blogitory.blog.posts.dto.response.GetPostForModifyResponseDto;
import com.blogitory.blog.posts.dto.response.GetPostResponseDto;
import com.blogitory.blog.posts.dto.response.GetRecentPostResponseDto;
import com.blogitory.blog.posts.entity.Posts;
import com.blogitory.blog.posts.entity.QPosts;
import com.blogitory.blog.posts.repository.PostsRepositoryCustom;
import com.blogitory.blog.poststag.entity.QPostsTag;
import com.blogitory.blog.tag.entity.QTag;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberPath;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.ArrayList;
import java.util.Comparator;
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
  QMember member = QMember.member;
  QCategory category = QCategory.category;
  QBlog blog = QBlog.blog;
  QPosts posts = QPosts.posts;
  QComment comment = QComment.comment;
  QHeart heart = QHeart.heart;
  QPostsTag postsTag = QPostsTag.postsTag;
  QTag tag = QTag.tag;


  /**
   * Constructor.
   *
   * @param jpaQueryFactory jpaQueryFactory
   */
  public PostsRepositoryImpl(JPAQueryFactory jpaQueryFactory) {
    super(Posts.class);
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
                    .where(posts.url.eq(url)
                            .and(posts.deleted.isFalse())
                            .and(posts.open.isTrue()))
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
            .where(member.left.isFalse()
                    .and(member.blocked.isFalse()))
            .where(blog.deleted.isFalse()
                    .and(posts.open.isTrue()))
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
  public Page<GetRecentPostResponseDto> getRecentPostByBlog(
          Pageable pageable, String blogUrl) {
    List<GetRecentPostResponseDto> postsList = recentPosts(pageable, blog.urlName.eq(blogUrl));
    long total = getPostsCountByBlog(blogUrl);

    return new PageImpl<>(postsList, pageable, total);
  }

  @Override
  public long getPostsCountByBlog(String blogUrl) {
    return from(posts)
            .select(posts.postsNo)
            .innerJoin(category).on(posts.category.categoryNo.eq(category.categoryNo))
            .innerJoin(blog).on(category.blog.blogNo.eq(blog.blogNo))
            .innerJoin(member).on(blog.member.memberNo.eq(member.memberNo))
            .where(member.left.isFalse().and(member.blocked.isFalse()))
            .where(blog.urlName.eq(blogUrl))
            .where(blog.deleted.isFalse())
            .where(category.deleted.isFalse())
            .where(posts.deleted.isFalse()
                    .and(posts.open.isTrue()))
            .fetchCount();
  }

  @Override
  public Page<GetRecentPostResponseDto> getRecentPostByCategory(
          Pageable pageable, String blogUrl, String categoryName) {
    List<GetRecentPostResponseDto> postList =
            recentPosts(pageable, blog.urlName.eq(blogUrl).and(category.name.eq(categoryName)));

    long total = from(posts)
            .select(posts.postsNo)
            .innerJoin(category).on(posts.category.categoryNo.eq(category.categoryNo))
            .innerJoin(blog).on(category.blog.blogNo.eq(blog.blogNo))
            .innerJoin(member).on(blog.member.memberNo.eq(member.memberNo))
            .where(blog.urlName.eq(blogUrl))
            .where(category.name.eq(categoryName))
            .where(member.left.isFalse().and(member.blocked.isFalse()))
            .where(blog.urlName.eq(blogUrl))
            .where(blog.deleted.isFalse())
            .where(category.deleted.isFalse())
            .where(posts.deleted.isFalse().and(posts.open.isTrue()))
            .fetchCount();

    return new PageImpl<>(postList, pageable, total);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<GetPopularPostResponseDto> getPopularPostsByBlog(String blogUrl) {
    NumberPath<Long> heartCnt = Expressions.numberPath(Long.class, "heartCnt");
    NumberPath<Long> commentCnt = Expressions.numberPath(Long.class, "commentCnt");

    List<GetPopularPostResponseDto> heartList = queryFactory
            .from(posts)
            .select(Projections.constructor(
                    GetPopularPostResponseDto.class,
                    posts.url,
                    posts.thumbnail,
                    posts.subject,
                    posts.summary,
                    ExpressionUtils.as(
                            JPAExpressions.select(heart.count())
                                    .from(heart)
                                    .where(heart.posts.postsNo.eq(posts.postsNo))
                                    .where(heart.deleted.isFalse()), "heartCnt"),
                    ExpressionUtils.as(
                            JPAExpressions.select(comment.count())
                                    .from(comment)
                                    .where(comment.posts.postsNo.eq(posts.postsNo))
                                    .where(comment.deleted.isFalse()), "commentCnt")
            ))
            .innerJoin(category).on(category.categoryNo.eq(posts.category.categoryNo))
            .innerJoin(blog).on(blog.blogNo.eq(category.blog.blogNo))
            .where(blog.urlName.eq(blogUrl))
            .where(posts.deleted.isFalse()
                    .and(posts.thumbnail.isNotEmpty())
                    .and(posts.open.isTrue()))
            .orderBy(heartCnt.desc())
            .limit(5)
            .fetch();

    List<GetPopularPostResponseDto> commentList = queryFactory
            .from(posts)
            .select(Projections.constructor(
                    GetPopularPostResponseDto.class,
                    posts.url,
                    posts.thumbnail,
                    posts.subject,
                    posts.summary,
                    ExpressionUtils.as(
                            JPAExpressions.select(heart.count())
                                    .from(heart)
                                    .where(heart.posts.postsNo.eq(posts.postsNo))
                                    .where(heart.deleted.isFalse()), "heartCnt"),
                    ExpressionUtils.as(
                            JPAExpressions.select(comment.count())
                                    .from(comment)
                                    .where(comment.posts.postsNo.eq(posts.postsNo))
                                    .where(comment.deleted.isFalse()), "commentCnt")
            ))
            .innerJoin(category).on(category.categoryNo.eq(posts.category.categoryNo))
            .innerJoin(blog).on(blog.blogNo.eq(category.blog.blogNo))
            .where(blog.urlName.eq(blogUrl))
            .where(posts.deleted.isFalse()
                    .and(posts.thumbnail.isNotEmpty())
                    .and(posts.open.isTrue()))
            .orderBy(commentCnt.desc())
            .limit(5)
            .fetch();

    List<GetPopularPostResponseDto> total = new ArrayList<>();
    total.addAll(heartList);
    total.addAll(commentList);

    return total.stream()
            .sorted(Comparator.comparingLong(r -> r.getCommentCnt() + r.getHeartCnt() * 2))
            .distinct()
            .limit(5)
            .toList();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Page<GetRecentPostResponseDto> getRecentPostsByTag(Pageable pageable,
                                                            String blogUrl,
                                                            String tagName) {
    List<GetRecentPostResponseDto> postList =
            from(posts)
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
            .leftJoin(postsTag).on(postsTag.posts.postsNo.eq(posts.postsNo))
            .leftJoin(tag).on(tag.tagNo.eq(postsTag.tag.tagNo))
            .where(member.left.isFalse()
                    .and(blog.deleted.isFalse())
                    .and(category.deleted.isFalse())
                    .and(posts.deleted.isFalse())
                    .and(posts.open.isTrue())
                    .and(member.blocked.isFalse())
                    .and(tag.name.eq(tagName)))
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .orderBy(posts.createdAt.desc())
            .fetch();

    long total = from(posts)
            .select(posts.postsNo)
            .innerJoin(category).on(posts.category.categoryNo.eq(category.categoryNo))
            .innerJoin(blog).on(category.blog.blogNo.eq(blog.blogNo))
            .innerJoin(member).on(blog.member.memberNo.eq(member.memberNo))
            .leftJoin(postsTag).on(postsTag.posts.postsNo.eq(posts.postsNo))
            .leftJoin(tag).on(tag.tagNo.eq(postsTag.tag.tagNo))
            .where(blog.urlName.eq(blogUrl)
                    .and(tag.name.eq(tagName))
                    .and(member.blocked.isFalse())
                    .and(member.left.isFalse())
                    .and(blog.urlName.eq(blogUrl))
                    .and(blog.deleted.isFalse())
                    .and(category.deleted.isFalse())
                    .and(posts.deleted.isFalse())
                    .and(posts.open.isTrue()))
            .fetchCount();

    return new PageImpl<>(postList, pageable, total);
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
            .where(member.left.isFalse()
                    .and(member.blocked.isFalse())
                    .and(blog.deleted.isFalse())
                    .and(category.deleted.isFalse())
                    .and(posts.deleted.isFalse())
                    .and(posts.open.isTrue()))
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .orderBy(posts.createdAt.desc())
            .fetch();
  }
}
