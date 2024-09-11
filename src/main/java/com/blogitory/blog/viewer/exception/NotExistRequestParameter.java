package com.blogitory.blog.viewer.exception;

/**
 * Not exist http servlet request parameter.
 *
 * @author woonseok
 * @Date 2024-09-09
 * @since 1.0
 **/
public class NotExistRequestParameter extends RuntimeException {
  public NotExistRequestParameter(String message) {
    super(message);
  }
}
