package com.blogitory.blog.posts.dto.response;

import com.blogitory.blog.tag.dto.GetTagResponseDto;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Get post response dto.
 *
 * @author woonseok
 * @since 1.0
 **/
@Getter
@NoArgsConstructor
public class GetPostResponseDto {
  private String username;
  private String memberName;
  private String blogName;
  private String blogUrl;
  private Long categoryNo;
  private String categoryName;
  private String postUrl;
  private String subject;
  private String postThumb;
  private String summary;
  private String detail;
  private Integer views;
  @JsonSerialize(using = LocalDateTimeSerializer.class)
  @JsonDeserialize(using = LocalDateTimeDeserializer.class)
  private LocalDateTime createdAt;
  @JsonSerialize(using = LocalDateTimeSerializer.class)
  @JsonDeserialize(using = LocalDateTimeDeserializer.class)
  private LocalDateTime updatedAt;

  @Setter
  private List<GetTagResponseDto> tags = new ArrayList<>();

  /**
   * Constructor.
   */
  public GetPostResponseDto(String username, String memberName,
                            String blogName, String blogUrl,
                            Long categoryNo, String categoryName,
                            String postUrl, String subject,
                            String postThumb,
                            String summary, String detail,
                            Integer views,
                            LocalDateTime createdAt, LocalDateTime updatedAt) {
    this.username = username;
    this.memberName = memberName;
    this.blogName = blogName;
    this.blogUrl = blogUrl;
    this.categoryNo = categoryNo;
    this.categoryName = categoryName;
    this.postUrl = postUrl;
    this.subject = subject;
    this.postThumb = postThumb;
    this.summary = summary;
    this.detail = detail;
    this.views = views;
    this.createdAt = createdAt;
    this.updatedAt = updatedAt;
  }
}
