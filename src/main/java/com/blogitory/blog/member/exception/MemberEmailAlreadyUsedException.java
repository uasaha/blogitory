package com.blogitory.blog.member.exception;

/**
 * MemberEmailAlreadyUsedException.
 *
 * @author woonseok
 * @since 1.0
 **/
public class MemberEmailAlreadyUsedException extends RuntimeException {
  private static final String MESSAGE = "Email Already used: ";

  public MemberEmailAlreadyUsedException(String email) {
    super(MESSAGE + email);
  }
}
