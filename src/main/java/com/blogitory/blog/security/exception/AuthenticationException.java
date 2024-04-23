package com.blogitory.blog.security.exception;

/**
 * An Exception during authenticate.
 *
 * @author woonseok
 * @since 1.0
 **/
public class AuthenticationException extends RuntimeException {
  public AuthenticationException() {
    super("아이디 혹은 비밀번호가 일치하지 않습니다.");
  }
}
