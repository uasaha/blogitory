package com.blogitory.blog.blog.repository.impl;

import com.blogitory.blog.blog.dto.BlogListInSettingsResponseDto;
import com.blogitory.blog.blog.entity.Blog;
import com.blogitory.blog.blog.entity.QBlog;
import com.blogitory.blog.blog.repository.BlogRepositoryCustom;
import com.blogitory.blog.image.entity.QImage;
import com.blogitory.blog.member.entity.QMember;
import com.querydsl.core.types.Projections;
import java.util.List;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

/**
 * Blog repository implementation.
 *
 * @author woonseok
 * @since 1.0
 **/
public class BlogRepositoryImpl extends QuerydslRepositorySupport implements BlogRepositoryCustom {
  public BlogRepositoryImpl() {
    super(Blog.class);
  }

  @Override
  public List<BlogListInSettingsResponseDto> getBlogListByMemberNo(Integer memberNo) {
    QBlog blog = QBlog.blog;
    QMember member = QMember.member;
    QImage image = QImage.image;

    return from(blog)
            .select(Projections.constructor(
                    BlogListInSettingsResponseDto.class,
                    blog.bio,
                    blog.urlName,
                    blog.intro,
                    blog.createdAt,
                    image.url,
                    image.originName))
            .innerJoin(blog.member, member)
            .leftJoin(image).on(image.blog.blogNo.eq(blog.blogNo))
            .where(blog.member.memberNo.eq(memberNo))
            .where(blog.deleted.eq(false))
            .fetch();
  }
}
