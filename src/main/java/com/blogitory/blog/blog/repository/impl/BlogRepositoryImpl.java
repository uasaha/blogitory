package com.blogitory.blog.blog.repository.impl;

import com.blogitory.blog.blog.dto.response.BlogListInSettingsResponseDto;
import com.blogitory.blog.blog.entity.Blog;
import com.blogitory.blog.blog.entity.QBlog;
import com.blogitory.blog.blog.repository.BlogRepositoryCustom;
import com.blogitory.blog.category.dto.CategorySettingsResponseDto;
import com.blogitory.blog.category.entity.QCategory;
import com.blogitory.blog.image.entity.QImage;
import com.blogitory.blog.member.entity.QMember;
import com.querydsl.core.group.GroupBy;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
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
  public List<BlogListInSettingsResponseDto> getBlogListByMemberNo(Integer memberNo) {
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
                            .list(Projections.constructor(BlogListInSettingsResponseDto.class,
                                    blog.name,
                                    blog.bio,
                                    blog.urlName,
                                    blog.createdAt,
                                    image.url,
                                    image.originName,
                                    GroupBy.list(Projections.constructor(
                                            CategorySettingsResponseDto.class,
                                            category.categoryNo,
                                            category.name,
                                            category.deleted
                                    ))))
            );
  }
}
