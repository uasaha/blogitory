package com.blogitory.blog.category.exception;

/**
 * Exception when create duplicated category.
 *
 * @author woonseok
 * @Date 2024-07-15
 * @since 1.0
 **/
public class DuplicateCategoryException extends RuntimeException {
  public DuplicateCategoryException(String name) {
    super("Duplicate category: " + name);
  }
}
