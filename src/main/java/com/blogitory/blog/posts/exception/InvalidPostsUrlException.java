package com.blogitory.blog.posts.exception;

/**
 * Invalid post url exception.
 *
 * @author woonseok
 * @Date 2024-08-04
 * @since 1.0
 **/
public class InvalidPostsUrlException extends RuntimeException {
  private static final String MESSAGE = "Invalid posts url";

  public InvalidPostsUrlException() {
    super(MESSAGE);
  }
}
