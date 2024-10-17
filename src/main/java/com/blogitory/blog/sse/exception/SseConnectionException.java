package com.blogitory.blog.sse.exception;

/**
 * SSE connection exception.
 *
 * @author woonseok
 * @since 1.0
 **/
public class SseConnectionException extends RuntimeException {

  private static final String MESSAGE = "SSE Connection failed";

  public SseConnectionException() {
    super(MESSAGE);
  }
}
