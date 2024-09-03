package com.blogitory.blog.posts.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDate;
import lombok.Getter;

/**
 * Get post activity response dto.
 *
 * @author woonseok
 * @Date 2024-09-02
 * @since 1.0
 **/
@Getter
public class GetPostActivityResponseDto {
  @JsonProperty("x")
  private final LocalDate date;

  @JsonProperty("y")
  private final long count;

  public GetPostActivityResponseDto(LocalDate date, long count) {
    this.date = date;
    this.count = Math.min(count, 5L);
  }
}
