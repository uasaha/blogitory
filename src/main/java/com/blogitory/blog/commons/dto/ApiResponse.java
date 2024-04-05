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
    this(true, 0, "", result);
  }

  /**
   * Constructor.
   *
   * @param isSuccessful  is Success
   * @param resultCode    service code
   * @param resultMessage service message
   * @param result        body
   */
  @Builder
  public ApiResponse(boolean isSuccessful, Integer resultCode, String resultMessage, T result) {
    this(new Header(isSuccessful, resultCode, resultMessage), result);
  }

  /**
   * Constructor.
   *
   * @param header isSuccessful, resultCode, resultMessage=
   * @param result body
   */
  public ApiResponse(Header header, T result) {
    this.header = header;
    this.result = result;
  }
}
