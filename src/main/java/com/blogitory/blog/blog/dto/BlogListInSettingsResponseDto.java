package com.blogitory.blog.blog.dto;

import java.time.LocalDateTime;
import java.util.Objects;
import lombok.Getter;

/**
 * Dto class for blog list in settings page.
 *
 * @author woonseok
 * @since 1.0
 **/
@Getter
public class BlogListInSettingsResponseDto {
  private final String blogBio;
  private final String blogUrl;
  private final String blogIntro;
  private final LocalDateTime createdAt;
  private boolean thumbIsNull;
  private final String thumbUrl;
  private final String thumbOriginName;

  /**
   * Constructor for Projections.
   *
   * @param blogBio        bio
   * @param blogUrl         url
   * @param blogIntro       intro
   * @param createdAt       createdAt
   * @param thumbUrl        thumbnail
   * @param thumbOriginName thumbnail origin name
   */
  public BlogListInSettingsResponseDto(String blogBio, String blogUrl,
                                       String blogIntro, LocalDateTime createdAt,
                                       String thumbUrl, String thumbOriginName) {
    this.blogBio = blogBio;
    this.blogUrl = blogUrl;
    this.blogIntro = blogIntro;
    this.createdAt = createdAt;
    this.thumbUrl = thumbUrl;
    this.thumbOriginName = thumbOriginName;
  }

  /**
   * Set thumbnail is null or empty.
   */
  public void setThumbIsNull() {
    if (Objects.isNull(thumbUrl) || thumbUrl.isEmpty()) {
      this.thumbIsNull = true;
    }
  }
}
