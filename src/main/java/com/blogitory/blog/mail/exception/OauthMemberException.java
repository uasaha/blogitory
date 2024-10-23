package com.blogitory.blog.mail.exception;

/**
 * OauthMemberException.
 *
 * @author woonseok
 * @since 1.0
 **/
public class OauthMemberException extends RuntimeException {

  public OauthMemberException() {
    super("Not provided for oauth member");
  }
}
