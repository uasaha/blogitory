package com.blogitory.blog.tag.entity;

/**
 * Tag dummy.
 *
 * @author woonseok
 * @since 1.0
 **/
public class TagDummy {
  public static Tag dummy() {
    return new Tag(1L, "tag", false);
  }
}