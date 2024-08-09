package com.blogitory.blog.posts.exception;

/**
 * Posts to json convert exception.
 *
 * @author woonseok
 * @Date 2024-08-01
 * @since 1.0
 **/
public class PostsJsonConvertException extends RuntimeException {
  private static final String MESSAGE = "Failed to convert posts to String";

  public PostsJsonConvertException() {
    super(MESSAGE);
  }
}
