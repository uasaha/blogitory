package com.blogitory.blog.category.entity;

import com.blogitory.blog.blog.entity.Blog;

/**
 * 설명 작성 필!
 *
 * @author woonseok
 * @since 1.0
 */
public class CategoryDummy {
  /**
   * Dummy category.
   *
   * @param blog the blog
   * @return the category
   */
  public static Category dummy(Blog blog) {
    return new Category(1L, blog, "DummyCategory", false);
  }
}
