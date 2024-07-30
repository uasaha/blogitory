package com.blogitory.blog.posts.entity;

import com.blogitory.blog.category.entity.Category;
import java.time.LocalDateTime;

/**
 * Posts dummy.
 *
 * @author woonseok
 * @since 1.0
 */
public class PostsDummy {
  /**
   * Dummy posts.
   *
   * @param category the category
   * @return the posts
   */
  public static Posts dummy(Category category) {
    return new Posts(
            1L, category,
            "posts_url",
            "subject", "summary",
            10,
            "thumbnail", "detail",
            LocalDateTime.of(2020, 12, 12, 12, 12, 12),
            true, false);
  }
}