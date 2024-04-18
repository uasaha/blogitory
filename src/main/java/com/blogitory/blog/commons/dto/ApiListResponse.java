package com.blogitory.blog.commons.dto;

import java.util.Collection;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * Dto for handling common response with collection body.
 *
 * @author woonseok
 * @since 1.0
 **/
@Getter
@Setter
public class ApiListResponse<T> {
  private final Header header;
  private final Collection<T> results;
  private final Integer totalCount;

  /**
   * Constructor.
   *
   * @param results    body
   * @param totalCount count of body
   */
  public ApiListResponse(Collection<T> results, Integer totalCount) {
    this(true, 200, "Success", results, totalCount);
  }

  /**
   * Constructor.
   *
   * @param isSuccessful  is Success
   * @param status    HTTP status code
   * @param resultMessage service message
   * @param results       body
   * @param totalCount    count of body
   */
  @Builder
  public ApiListResponse(boolean isSuccessful,
                         Integer status,
                         String resultMessage,
                         Collection<T> results,
                         Integer totalCount) {
    this(new Header(isSuccessful, status, resultMessage), results, totalCount);
  }

  /**
   * Constructor.
   *
   * @param header     isSuccessful, resultCode, resultMessage
   * @param results    body
   * @param totalCount count of body
   */
  public ApiListResponse(Header header, Collection<T> results, Integer totalCount) {
    this.header = header;
    this.results = results;
    this.totalCount = totalCount;
  }
}
