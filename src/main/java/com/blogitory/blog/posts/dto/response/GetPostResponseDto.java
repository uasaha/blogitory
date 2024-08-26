package com.blogitory.blog.posts.dto.response;

import com.blogitory.blog.tag.dto.GetTagResponseDto;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

/**
 * Get post response dto.
 *
 * @author woonseok
 * @Date 2024-08-05
 * @since 1.0
 **/
@Getter
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
  private LocalDateTime createdAt;
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
