package com.blogitory.blog.commons.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * Dto for handling common response.
 *
 * @author woonseok
 * @since 1.0
 **/
@Getter
@Setter
public class ApiResponse<T> {
  private final Header header;
  private final T result;

  /**
   * Constructor.
   *
   * @param result body
   */
  public ApiResponse(T result) {
    this(true, 200, "Success", result);
  }

  /**
   * Constructor.
   *
   * @param isSuccessful  is Success
   * @param status HTTP status code
   * @param resultMessage service message
   * @param result        body
   */
  @Builder
  public ApiResponse(boolean isSuccessful, Integer status, String resultMessage, T result) {
    this(new Header(isSuccessful, status, resultMessage), result);
  }

  /**
   * Constructor.
   *
   * @param header isSuccessful, status, resultMessage=
   * @param result body
   */
  public ApiResponse(Header header, T result) {
    this.header = header;
    this.result = result;
  }
}
