package com.blogitory.blog.tag.repository.impl;

import com.blogitory.blog.posts.entity.QPosts;
import com.blogitory.blog.poststag.entity.QPostsTag;
import com.blogitory.blog.tag.dto.GetTagResponseDto;
import com.blogitory.blog.tag.entity.QTag;
import com.blogitory.blog.tag.entity.Tag;
import com.blogitory.blog.tag.repository.TagRepositoryCustom;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPQLQueryFactory;
import java.util.List;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

/**
 * Tag repository implementation for using querydsl.
 *
 * @author woonseok
 * @Date 2024-08-05
 * @since 1.0
 **/
public class TagRepositoryImpl extends QuerydslRepositorySupport implements TagRepositoryCustom {
  private final JPQLQueryFactory queryFactory;

  public TagRepositoryImpl(JPQLQueryFactory queryFactory) {
    super(Tag.class);
    this.queryFactory = queryFactory;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<GetTagResponseDto> getTagListByPost(String postUrl) {
    QTag tag = QTag.tag;
    QPostsTag postsTag = QPostsTag.postsTag;
    QPosts posts = QPosts.posts;

    return queryFactory.from(tag)
            .select(Projections.constructor(
                    GetTagResponseDto.class,
                    tag.name))
            .innerJoin(postsTag).on(tag.tagNo.eq(postsTag.tag.tagNo))
            .innerJoin(posts).on(posts.postsNo.eq(postsTag.posts.postsNo))
            .where(posts.url.eq(postUrl))
            .where(tag.deleted.eq(false))
            .fetch();
  }
}
