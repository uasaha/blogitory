package com.blogitory.blog.poststag.entity;

import com.blogitory.blog.blog.entity.Blog;
import com.blogitory.blog.posts.entity.Posts;
import com.blogitory.blog.tag.entity.Tag;

/**
 * PostsTag dummy.
 *
 * @author woonseok
 * @since 1.0
 */
public class PostsTagDummy {
  /**
   * Dummy posts tag.
   *
   * @param tag   the tag
   * @param posts the posts
   * @param blog  the blog
   * @return the posts tag
   */
  public static PostsTag dummy(Tag tag, Posts posts, Blog blog) {
    return new PostsTag(1L, tag, posts, blog);
  }
}