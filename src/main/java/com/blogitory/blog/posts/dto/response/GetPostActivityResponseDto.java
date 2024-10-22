package com.blogitory.blog.posts.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import java.time.LocalDate;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Get post activity response dto.
 *
 * @author woonseok
 * @since 1.0
 **/
@Getter
@NoArgsConstructor
public class GetPostActivityResponseDto {
  @JsonProperty("x")
  @JsonSerialize(using = LocalDateSerializer.class)
  @JsonDeserialize(using = LocalDateDeserializer.class)
  private LocalDate date;

  @JsonProperty("y")
  private long count;

  public GetPostActivityResponseDto(LocalDate date, long count) {
    this.date = date;
    this.count = Math.min(count, 5L);
  }
}
