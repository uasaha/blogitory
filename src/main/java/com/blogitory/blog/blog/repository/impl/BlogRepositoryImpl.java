package com.blogitory.blog.blog.repository.impl;

import com.blogitory.blog.blog.dto.response.GetBlogInHeaderResponseDto;
import com.blogitory.blog.blog.dto.response.GetBlogInSettingsResponseDto;
import com.blogitory.blog.blog.dto.response.GetBlogResponseDto;
import com.blogitory.blog.blog.dto.response.GetBlogWithCategoryResponseDto;
import com.blogitory.blog.blog.entity.Blog;
import com.blogitory.blog.blog.entity.QBlog;
import com.blogitory.blog.blog.repository.BlogRepositoryCustom;
import com.blogitory.blog.category.dto.GetCategoryInSettingsResponseDto;
import com.blogitory.blog.category.dto.GetCategoryResponseDto;
import com.blogitory.blog.category.entity.QCategory;
import com.blogitory.blog.image.entity.QImage;
import com.blogitory.blog.member.entity.QMember;
import com.blogitory.blog.poststag.entity.QPostsTag;
import com.blogitory.blog.tag.dto.GetTagResponseDto;
import com.blogitory.blog.tag.entity.QTag;
import com.querydsl.core.group.GroupBy;
import com.querydsl.core.types.Projections;
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
            .where(blog.member.memberNo.eq(memberNo))
            .where(blog.deleted.eq(false))
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
            .where(member.username.eq(username))
            .where(blog.deleted.eq(false))
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
    QCategory category = QCategory.category;
    QTag tag = QTag.tag;
    QPostsTag postsTag = QPostsTag.postsTag;

    List<GetBlogResponseDto> response = queryFactory.from(blog)
            .select(blog)
            .innerJoin(blog.member, member)
            .leftJoin(image).on(image.blog.blogNo.eq(blog.blogNo))
            .leftJoin(category).on(blog.blogNo.eq(category.blog.blogNo))
            .leftJoin(postsTag).on(postsTag.blog.blogNo.eq(blog.blogNo))
            .leftJoin(tag).on(postsTag.tag.tagNo.eq(tag.tagNo))
            .where(blog.urlName.eq(url))
            .where(blog.deleted.eq(false))
            .transform(
                    GroupBy.groupBy(blog.blogNo)
                            .list(
                                    Projections.constructor(
                                            GetBlogResponseDto.class,
                                            image.url,
                                            image.originName,
                                            blog.urlName,
                                            blog.name,
                                            member.name,
                                            member.username,
                                            blog.bio,
                                            GroupBy.list(
                                                    Projections.constructor(
                                                            GetCategoryResponseDto.class,
                                                            category.categoryNo,
                                                            category.name,
                                                            category.deleted
                                                    )),
                                            GroupBy.list(
                                                    Projections.constructor(
                                                            GetTagResponseDto.class,
                                                            tag.name))
                                    )));

    return response.isEmpty() ? Optional.empty() : Optional.of(response.getFirst());
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
            .where(blog.member.memberNo.eq(memberNo))
            .where(blog.deleted.eq(false))
            .orderBy(blog.blogNo.asc())
            .transform(
                    GroupBy.groupBy(blog.blogNo)
                            .list(Projections.constructor(GetBlogWithCategoryResponseDto.class,
                                    blog.blogNo,
                                    blog.name,
                                    GroupBy.list(Projections.constructor(
                                            GetCategoryResponseDto.class,
                                            category.categoryNo,
                                            category.name,
                                            category.deleted
                                    )))));
  }
}
