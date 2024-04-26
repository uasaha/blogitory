package com.blogitory.blog.posts.entity;

import com.blogitory.blog.category.entity.Category;
import java.time.LocalDateTime;

/**
 * Posts dummy.
 *
 * @author woonseok
 * @since 1.0
 **/
public class PostsDummy {
  public static Posts dummy(Category category) {
    return new Posts(
            1L, category,
            "subject", "summary",
            "thumbnail", "detail",
            LocalDateTime.of(2020, 12, 12, 12, 12, 12),
            true, 1L, false);
  }
}