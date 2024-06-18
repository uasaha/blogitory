package com.blogitory.blog.security.exception;

/**
 * An Exception during authenticate.
 *
 * @author woonseok
 * @since 1.0
 **/
public class AuthenticationException extends RuntimeException {
  public AuthenticationException(String msg) {
    super(msg);
  }
}
