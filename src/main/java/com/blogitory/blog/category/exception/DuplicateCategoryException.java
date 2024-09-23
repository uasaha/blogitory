package com.blogitory.blog.category.exception;

/**
 * Exception when create duplicated category.
 *
 * @author woonseok
 * @since 1.0
 **/
public class DuplicateCategoryException extends RuntimeException {
  public DuplicateCategoryException(String name) {
    super("Duplicate category: " + name);
  }
}
