package com.blogitory.blog.mail.exception;

/**
 * Exception that occurs when email for signup is not verified.
 *
 * @author woonseok
 * @since 1.0
 **/
public class EmailNotVerificationException extends RuntimeException {
  private static final String MESSAGE = "Email not verified. Email: ";

  public EmailNotVerificationException(String email) {
    super(MESSAGE + email);
  }
}
