package com.blogitory.blog.visitant.entity;

import com.blogitory.blog.blog.entity.Blog;
import java.time.LocalDate;

/**
 * Visitant entity dummy.
 *
 * @author woonseok
 * @Date 2024-09-11
 * @since 1.0
 **/
public class VisitantDummy {
  public static Visitant dummy(Blog blog) {
    return new Visitant(1L, blog, 0, LocalDate.now());
  }
}
