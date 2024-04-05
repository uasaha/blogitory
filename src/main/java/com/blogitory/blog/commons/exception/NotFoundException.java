package com.blogitory.blog.commons.exception;

/**
 * Exception that occurs when there are no result when querying DB.
 *
 * @author woonseok
 * @since 1.0
 **/
public class NotFoundException extends RuntimeException {
  private static final String MESSAGE = "Not Found Exception. Type: ";

  /**
   * Constructor.
   *
   * @param clazz class
   */
  public NotFoundException(Class<?> clazz) {
    super(MESSAGE + clazz.getName());
  }
}
