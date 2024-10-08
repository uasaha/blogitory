package com.blogitory.blog.comment.repository.impl;

import com.blogitory.blog.blog.entity.QBlog;
import com.blogitory.blog.category.entity.QCategory;
import com.blogitory.blog.comment.dto.response.GetChildCommentResponseDto;
import com.blogitory.blog.comment.dto.response.GetCommentResponseDto;
import com.blogitory.blog.comment.dto.response.GetLatestCommentListResponseDto;
import com.blogitory.blog.comment.entity.Comment;
import com.blogitory.blog.comment.entity.QComment;
import com.blogitory.blog.comment.repository.CommentRepositoryCustom;
import com.blogitory.blog.member.entity.QMember;
import com.blogitory.blog.posts.entity.QPosts;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.JPQLQueryFactory;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.data.support.PageableExecutionUtils;

/**
 * Comment Repository for using querydsl.
 *
 * @author woonseok
 * @since 1.0
 **/
public class CommentRepositoryImpl
        extends QuerydslRepositorySupport implements CommentRepositoryCustom {

  private final JPQLQueryFactory queryFactory;

  public CommentRepositoryImpl(JPQLQueryFactory queryFactory) {
    super(Comment.class);
    this.queryFactory = queryFactory;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Page<GetCommentResponseDto> getComments(String postsUrl, Pageable pageable) {
    QComment comment = QComment.comment;
    QComment child = new QComment("child");
    QMember member = QMember.member;
    QPosts posts = QPosts.posts;

    List<GetCommentResponseDto> comments = queryFactory
            .from(comment)
            .select(Projections.constructor(
                    GetCommentResponseDto.class,
                    member.name,
                    member.username,
                    member.profileThumb,
                    comment.commentNo,
                    comment.contents,
                    comment.deleted,
                    comment.createdAt,
                    comment.updatedAt,
                    JPAExpressions.select(child.count())
                            .from(child)
                            .where(child.parentComment.commentNo.eq(comment.commentNo))))
            .innerJoin(member).on(member.memberNo.eq(comment.member.memberNo))
            .innerJoin(posts).on(posts.url.eq(comment.posts.url))
            .orderBy(comment.createdAt.desc())
            .where(comment.posts.url.eq(postsUrl))
            .where(posts.deleted.isFalse()
                    .and(posts.open.isTrue())
                    .and(comment.parentComment.isNull()))
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .fetch();

    JPQLQuery<Long> count = from(comment)
            .select(comment.count())
            .where(comment.posts.url.eq(postsUrl))
            .where(posts.deleted.isFalse()
                    .and(posts.open.isTrue())
                    .and(comment.parentComment.isNull()));

    return PageableExecutionUtils.getPage(comments, pageable, count::fetchOne);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Long getCommentCountByPost(String postUrl) {
    QComment comment = QComment.comment;
    QPosts posts = QPosts.posts;

    return queryFactory
            .from(comment)
            .select(comment.count())
            .innerJoin(posts).on(posts.postsNo.eq(comment.posts.postsNo))
            .where(posts.url.eq(postUrl))
            .where(posts.deleted.isFalse().and(posts.open.isTrue()))
            .fetchFirst();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Page<GetChildCommentResponseDto> getChildCommentsByParent(String postsUrl,
                                                                   Long commentNo,
                                                                   Pageable pageable) {
    QComment comment = QComment.comment;
    QComment parent = new QComment("parent");
    QMember member = QMember.member;
    QPosts posts = QPosts.posts;

    List<GetChildCommentResponseDto> comments = queryFactory
            .from(comment)
            .select(Projections.constructor(
                    GetChildCommentResponseDto.class,
                    member.name,
                    member.username,
                    member.profileThumb,
                    parent.commentNo,
                    comment.commentNo,
                    comment.contents,
                    comment.deleted,
                    comment.createdAt,
                    comment.updatedAt))
            .innerJoin(member).on(member.memberNo.eq(comment.member.memberNo))
            .innerJoin(parent).on(comment.parentComment.commentNo.eq(parent.commentNo))
            .innerJoin(posts).on(posts.url.eq(comment.posts.url))
            .orderBy(comment.createdAt.desc())
            .where(comment.posts.url.eq(postsUrl))
            .where(comment.parentComment.commentNo.eq(commentNo))
            .where(posts.deleted.isFalse()
                    .and(posts.open.isTrue()))
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .fetch();

    JPQLQuery<Long> count = from(comment)
            .select(comment.count())
            .where(posts.deleted.isFalse()
                    .and(posts.open.isTrue())
                    .and(comment.parentComment.commentNo.eq(commentNo))
                    .and(comment.posts.url.eq(postsUrl)));

    return PageableExecutionUtils.getPage(comments, pageable, count::fetchOne);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<GetLatestCommentListResponseDto> getRecentCommentsByBlog(
          String username, String blogUrl) {
    QComment comment = QComment.comment;
    QBlog blog = QBlog.blog;
    QCategory category = QCategory.category;
    QMember member = QMember.member;
    QPosts posts = QPosts.posts;

    return queryFactory
            .from(comment)
            .select(Projections.constructor(
                    GetLatestCommentListResponseDto.class,
                    member.name,
                    member.username,
                    member.profileThumb,
                    posts.url,
                    comment.contents,
                    comment.createdAt))
            .innerJoin(member).on(member.memberNo.eq(comment.member.memberNo))
            .innerJoin(posts).on(posts.url.eq(comment.posts.url))
            .innerJoin(category).on(category.categoryNo.eq(posts.category.categoryNo))
            .innerJoin(blog).on(blog.blogNo.eq(category.blog.blogNo))
            .orderBy(comment.createdAt.desc())
            .where(comment.deleted.isFalse()
                    .and(posts.deleted.isFalse())
                    .and(posts.open.isTrue()))
            .where(blog.urlName.eq(blogUrl))
            .where(comment.member.username.eq(username).not())
            .where(comment.parentComment.isNull())
            .limit(4L)
            .fetch();
  }
}
