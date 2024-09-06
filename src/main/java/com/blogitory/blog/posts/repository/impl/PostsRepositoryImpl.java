package com.blogitory.blog.posts.repository.impl;

import com.blogitory.blog.blog.entity.QBlog;
import com.blogitory.blog.category.entity.QCategory;
import com.blogitory.blog.comment.entity.QComment;
import com.blogitory.blog.follow.entity.QFollow;
import com.blogitory.blog.heart.entity.QHeart;
import com.blogitory.blog.member.entity.QMember;
import com.blogitory.blog.posts.dto.response.GetFeedPostsResponseDto;
import com.blogitory.blog.posts.dto.response.GetPopularPostResponseDto;
import com.blogitory.blog.posts.dto.response.GetPostActivityResponseDto;
import com.blogitory.blog.posts.dto.response.GetPostForModifyResponseDto;
import com.blogitory.blog.posts.dto.response.GetPostManageResponseDto;
import com.blogitory.blog.posts.dto.response.GetPostResponseDto;
import com.blogitory.blog.posts.dto.response.GetRecentPostResponseDto;
import com.blogitory.blog.posts.entity.Posts;
import com.blogitory.blog.posts.entity.QPosts;
import com.blogitory.blog.posts.repository.PostsRepositoryCustom;
import com.blogitory.blog.poststag.entity.QPostsTag;
import com.blogitory.blog.tag.entity.QTag;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.DateExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberPath;
import com.querydsl.core.types.dsl.NumberTemplate;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
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

  private static final String HEART_CNT = "heartCnt";
  private static final String COMMENT_CNT = "commentCnt";

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

  /**
   * {@inheritDoc}
   */
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

  /**
   * {@inheritDoc}
   */
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
    NumberPath<Long> heartCnt = Expressions.numberPath(Long.class, HEART_CNT);
    NumberPath<Long> commentCnt = Expressions.numberPath(Long.class, COMMENT_CNT);

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
                                    .where(heart.deleted.isFalse()), HEART_CNT),
                    ExpressionUtils.as(
                            JPAExpressions.select(comment.count())
                                    .from(comment)
                                    .where(comment.posts.postsNo.eq(posts.postsNo))
                                    .where(comment.deleted.isFalse()), COMMENT_CNT)
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
                                    .where(heart.deleted.isFalse()), HEART_CNT),
                    ExpressionUtils.as(
                            JPAExpressions.select(comment.count())
                                    .from(comment)
                                    .where(comment.posts.postsNo.eq(posts.postsNo))
                                    .where(comment.deleted.isFalse()), COMMENT_CNT)
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
                                    .where(comment.posts.postsNo.eq(posts.postsNo))))
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
   * {@inheritDoc}
   */
  @Override
  public Page<GetPostManageResponseDto> getPostsByMemberNo(Pageable pageable, Integer memberNo) {
    List<GetPostManageResponseDto> postList = queryFactory
            .from(posts)
            .select(Projections.constructor(
                    GetPostManageResponseDto.class,
                    blog.name,
                    category.name,
                    posts.url,
                    posts.subject,
                    posts.thumbnail,
                    posts.createdAt,
                    posts.open))
            .innerJoin(category).on(category.categoryNo.eq(posts.category.categoryNo))
            .innerJoin(blog).on(blog.blogNo.eq(category.blog.blogNo))
            .innerJoin(member).on(member.memberNo.eq(blog.member.memberNo))
            .where(member.memberNo.eq(memberNo))
            .where(member.blocked.isFalse().and(member.left.isFalse()))
            .where(blog.deleted.isFalse())
            .where(category.deleted.isFalse())
            .where(posts.deleted.isFalse())
            .orderBy(posts.createdAt.desc())
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .fetch();

    long total = from(posts)
            .select(posts.postsNo)
            .innerJoin(category).on(category.categoryNo.eq(posts.category.categoryNo))
            .innerJoin(blog).on(blog.blogNo.eq(category.blog.blogNo))
            .innerJoin(member).on(member.memberNo.eq(blog.member.memberNo))
            .where(member.memberNo.eq(memberNo))
            .where(member.blocked.isFalse().and(member.left.isFalse()))
            .where(blog.deleted.isFalse())
            .where(category.deleted.isFalse())
            .where(posts.deleted.isFalse())
            .fetchCount();

    return new PageImpl<>(postList, pageable, total);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Page<GetRecentPostResponseDto> getPostsByHearts(Integer memberNo, Pageable pageable) {
    QMember target = new QMember("target");

    List<GetRecentPostResponseDto> postList =
            from(heart)
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
                                    .where(comment.posts.postsNo.eq(posts.postsNo))))
                    .innerJoin(posts).on(posts.postsNo.eq(heart.posts.postsNo))
                    .innerJoin(target).on(target.memberNo.eq(heart.member.memberNo))
                    .innerJoin(category).on(category.categoryNo.eq(posts.category.categoryNo))
                    .innerJoin(blog).on(blog.blogNo.eq(category.blog.blogNo))
                    .innerJoin(member).on(member.memberNo.eq(blog.member.memberNo))
                    .where(heart.deleted.isFalse())
                    .where(target.memberNo.eq(memberNo))
                    .where(posts.deleted.isFalse().and(posts.open.isTrue()))
                    .where(category.deleted.isFalse())
                    .where(blog.deleted.isFalse())
                    .where(member.blocked.isFalse().and(target.blocked.isFalse()))
                    .orderBy(heart.createdAt.desc())
                    .offset(pageable.getOffset())
                    .limit(pageable.getPageSize())
                    .fetch();

    long total = from(heart)
            .select(heart.posts.postsNo)
            .innerJoin(posts).on(posts.postsNo.eq(heart.posts.postsNo))
            .innerJoin(target).on(target.memberNo.eq(heart.member.memberNo))
            .innerJoin(category).on(category.categoryNo.eq(posts.category.categoryNo))
            .innerJoin(blog).on(blog.blogNo.eq(category.blog.blogNo))
            .innerJoin(member).on(member.memberNo.eq(blog.member.memberNo))
            .where(heart.deleted.isFalse())
            .where(target.memberNo.eq(memberNo))
            .where(posts.deleted.isFalse().and(posts.open.isTrue()))
            .where(category.deleted.isFalse())
            .where(blog.deleted.isFalse())
            .where(member.blocked.isFalse().and(target.blocked.isFalse()))
            .fetchCount();

    return new PageImpl<>(postList, pageable, total);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<GetPostActivityResponseDto> getPostActivity(String username,
                                                          LocalDate start,
                                                          LocalDate end) {

    DateExpression<Date> createdAtDate = Expressions.dateTemplate(
            Date.class, "DATE({0})", posts.createdAt);

    List<Tuple> result = queryFactory
            .from(posts)
            .select(
                    createdAtDate,
                    posts.count())
            .innerJoin(category).on(posts.category.categoryNo.eq(category.categoryNo))
            .innerJoin(blog).on(category.blog.blogNo.eq(blog.blogNo))
            .innerJoin(member).on(blog.member.memberNo.eq(member.memberNo))
            .where(member.username.eq(username))
            .where(posts.createdAt.after(start.atTime(0, 0)))
            .where(posts.createdAt.before(end.atTime(0, 0)))
            .where(member.blocked.isFalse().and(member.left.isFalse()))
            .where(blog.deleted.isFalse())
            .where(category.deleted.isFalse())
            .where(posts.deleted.isFalse())
            .groupBy(createdAtDate)
            .orderBy(posts.createdAt.asc())
            .fetch();

    List<GetPostActivityResponseDto> resultDto = new ArrayList<>();

    result.forEach(post -> {
      LocalDate localDate = Objects.requireNonNull(post.get(createdAtDate)).toLocalDate();
      long count = Objects.requireNonNull(post.get(posts.count()));

      resultDto.add(new GetPostActivityResponseDto(localDate, count));
    });

    return resultDto;
  }

  @Override
  public Page<GetRecentPostResponseDto> searchPosts(Pageable pageable, String words) {
    NumberTemplate<Double> match =
            Expressions.numberTemplate(Double.class,
                    "function('match',{0}, {1}, {2})",
                    posts.subject, posts.detail, "'" + words + "'");

    List<GetRecentPostResponseDto> content = queryFactory
            .from(posts)
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
                            .where(comment.posts.postsNo.eq(posts.postsNo))))
            .innerJoin(category).on(posts.category.categoryNo.eq(category.categoryNo))
            .innerJoin(blog).on(category.blog.blogNo.eq(blog.blogNo))
            .innerJoin(member).on(blog.member.memberNo.eq(member.memberNo))
            .where(match.gt(0.00))
            .where(member.left.isFalse()
                    .and(member.blocked.isFalse())
                    .and(blog.deleted.isFalse())
                    .and(category.deleted.isFalse())
                    .and(posts.deleted.isFalse())
                    .and(posts.open.isTrue()))
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .fetch();

    long total = from(posts)
            .select(posts.postsNo.count())
            .innerJoin(category).on(posts.category.categoryNo.eq(category.categoryNo))
            .innerJoin(blog).on(category.blog.blogNo.eq(blog.blogNo))
            .innerJoin(member).on(blog.member.memberNo.eq(member.memberNo))
            .where(match.gt(0.00))
            .where(member.left.isFalse()
                    .and(member.blocked.isFalse())
                    .and(blog.deleted.isFalse())
                    .and(category.deleted.isFalse())
                    .and(posts.deleted.isFalse())
                    .and(posts.open.isTrue()))
            .fetchCount();

    return new PageImpl<>(content, pageable, total);
  }

  @Override
  public Long getFeedStartPostsNoByMemberNo(Integer memberNo) {
    QFollow follow = QFollow.follow;
    QMember followee = new QMember("followee");

    return queryFactory.from(follow)
            .select(posts.postsNo)
            .innerJoin(member).on(follow.followFrom.memberNo.eq(member.memberNo))
            .innerJoin(followee).on(follow.followTo.memberNo.eq(followee.memberNo))
            .innerJoin(blog).on(blog.member.memberNo.eq(followee.memberNo))
            .innerJoin(category).on(category.blog.blogNo.eq(blog.blogNo))
            .innerJoin(posts).on(posts.category.categoryNo.eq(category.categoryNo))
            .where(member.memberNo.eq(memberNo))
            .where(member.blocked.isFalse().and(member.left.isFalse()))
            .where(blog.deleted.isFalse())
            .where(category.deleted.isFalse())
            .where(posts.deleted.isFalse().and(posts.open.isTrue()))
            .orderBy(posts.postsNo.desc())
            .limit(1L)
            .fetchOne();
  }

  @Override
  public Page<GetFeedPostsResponseDto> getFeedPostsByMemberNo(Integer memberNo,
                                                              Long start,
                                                              Pageable pageable) {
    QFollow follow = QFollow.follow;
    QMember followee = new QMember("followee");

    List<GetFeedPostsResponseDto> postList = queryFactory.from(follow)
            .select(Projections.constructor(
                    GetFeedPostsResponseDto.class,
                    followee.username,
                    posts.subject,
                    posts.createdAt,
                    posts.updatedAt,
                    posts.detail,
                    posts.url,
                    blog.urlName,
                    blog.name,
                    blog.background
            ))
            .innerJoin(member).on(follow.followFrom.memberNo.eq(member.memberNo))
            .innerJoin(followee).on(follow.followTo.memberNo.eq(followee.memberNo))
            .innerJoin(blog).on(blog.member.memberNo.eq(followee.memberNo))
            .innerJoin(category).on(category.blog.blogNo.eq(blog.blogNo))
            .innerJoin(posts).on(posts.category.categoryNo.eq(category.categoryNo))
            .where(member.memberNo.eq(memberNo))
            .where(posts.postsNo.loe(start))
            .where(member.blocked.isFalse().and(member.left.isFalse()))
            .where(blog.deleted.isFalse())
            .where(category.deleted.isFalse())
            .where(posts.deleted.isFalse().and(posts.open.isTrue()))
            .orderBy(posts.postsNo.desc())
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .fetch();

    long total = from(follow)
            .select(posts.postsNo.count())
            .innerJoin(member).on(follow.followFrom.memberNo.eq(member.memberNo))
            .innerJoin(followee).on(follow.followTo.memberNo.eq(followee.memberNo))
            .innerJoin(blog).on(blog.member.memberNo.eq(followee.memberNo))
            .innerJoin(category).on(category.blog.blogNo.eq(blog.blogNo))
            .innerJoin(posts).on(posts.category.categoryNo.eq(category.categoryNo))
            .where(member.memberNo.eq(memberNo))
            .where(posts.postsNo.loe(start))
            .where(member.blocked.isFalse().and(member.left.isFalse()))
            .where(blog.deleted.isFalse())
            .where(category.deleted.isFalse())
            .where(posts.deleted.isFalse().and(posts.open.isTrue()))
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
                            .where(comment.posts.postsNo.eq(posts.postsNo))))
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
