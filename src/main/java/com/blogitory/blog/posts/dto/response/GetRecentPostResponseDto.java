package com.blogitory.blog.posts.dto.response;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Get Recent Post Response dto.
 *
 * @author woonseok
 * @since 1.0
 **/
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class GetRecentPostResponseDto {
  private String blogUrl;
  private String blogName;
  private String username;
  private String blogPfp;
  private String postUrl;
  private String title;
  private String summary;
  private String thumb;

  @JsonSerialize(using = LocalDateTimeSerializer.class)
  @JsonDeserialize(using = LocalDateTimeDeserializer.class)
  private LocalDateTime createdAt;
  private Long heart;
  private Long comment;
}
