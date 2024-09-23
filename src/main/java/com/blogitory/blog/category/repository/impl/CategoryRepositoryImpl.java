package com.blogitory.blog.category.repository.impl;

import com.blogitory.blog.blog.entity.QBlog;
import com.blogitory.blog.category.dto.GetCategoryResponseDto;
import com.blogitory.blog.category.entity.Category;
import com.blogitory.blog.category.entity.QCategory;
import com.blogitory.blog.category.repository.CategoryRepositoryCustom;
import com.blogitory.blog.posts.entity.QPosts;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

/**
 * Implementation of category repository for using querydsl.
 *
 * @author woonseok
 * @since 1.0
 **/
public class CategoryRepositoryImpl
        extends QuerydslRepositorySupport implements CategoryRepositoryCustom {

  private final JPAQueryFactory queryFactory;

  public CategoryRepositoryImpl(JPAQueryFactory queryFactory) {
    super(Category.class);
    this.queryFactory = queryFactory;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<GetCategoryResponseDto> getCategoriesByBlog(String blogUrl) {
    QBlog blog = QBlog.blog;
    QCategory category = QCategory.category;
    QPosts posts = QPosts.posts;

    return queryFactory
            .from(category)
            .select(Projections.constructor(
                    GetCategoryResponseDto.class,
                    category.categoryNo,
                    category.name,
                    category.deleted,
                    ExpressionUtils.as(
                            JPAExpressions.select(posts.count())
                                    .from(posts)
                                    .where(posts.category.eq(category)
                                            .and(posts.deleted.isFalse())
                                            .and(posts.open.isTrue())),
                            "postsCnt"
                    )))
            .innerJoin(blog).on(category.blog.blogNo.eq(blog.blogNo))
            .leftJoin(posts).on(posts.category.categoryNo.eq(category.categoryNo))
            .where(blog.urlName.eq(blogUrl)
                    .and(blog.deleted.isFalse())
                    .and(category.deleted.isFalse()))
            .fetch();
  }
}
