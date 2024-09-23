package com.blogitory.blog.blog.exception;

/**
 * Blog limit reach exception.
 *
 * @author woonseok
 * @since 1.0
 **/
public class BlogLimitException extends RuntimeException {
  private static final String BLOG_LIMIT_REACHED = "Blog limit reached";

  /**
   * Constructor.
   */
  public BlogLimitException() {
    super(BLOG_LIMIT_REACHED);
  }
}
