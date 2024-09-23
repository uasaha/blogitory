package com.blogitory.blog.member.exception;

/**
 * Exception when failed modify password.
 *
 * @author woonseok
 * @since 1.0
 **/
public class MemberPwdChangeFailedException extends RuntimeException {
  private static final String MESSAGE = "비밀번호 변경에 실패하였습니다.";

  /**
   * Constructor.
   */
  public MemberPwdChangeFailedException() {
    super(MESSAGE);
  }
}
