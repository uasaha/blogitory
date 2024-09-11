package com.blogitory.blog.viewer.entity;

import com.blogitory.blog.posts.entity.Posts;
import java.time.LocalDate;

/**
 * Viewer Dummy.
 *
 * @author woonseok
 * @Date 2024-09-11
 * @since 1.0
 **/
public class ViewerDummy {
  public static Viewer dummy(Posts posts) {
    return new Viewer(1L, posts, 0, LocalDate.now());
  }
}
