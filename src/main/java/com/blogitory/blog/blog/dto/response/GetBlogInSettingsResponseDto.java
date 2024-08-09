package com.blogitory.blog.blog.dto.response;

import com.blogitory.blog.category.dto.GetCategoryInSettingsResponseDto;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import lombok.Getter;

/**
 * Dto class for blog list in settings page.
 *
 * @author woonseok
 * @since 1.0
 **/
@Getter
public class GetBlogInSettingsResponseDto {
  private final String blogName;
  private final String blogBio;
  private final String blogUrl;
  private final LocalDateTime createdAt;
  private boolean thumbIsNull;
  private final String thumbUrl;
  private final String thumbOriginName;
  private final List<GetCategoryInSettingsResponseDto> categories;

  /**
   * Constructor for Projections.
   *
   * @param blogBio        bio
   * @param blogUrl         url
   * @param createdAt       createdAt
   * @param thumbUrl        thumbnail
   * @param thumbOriginName thumbnail origin name
   */
  public GetBlogInSettingsResponseDto(String blogName, String blogBio, String blogUrl,
                                      LocalDateTime createdAt,
                                      String thumbUrl, String thumbOriginName,
                                      List<GetCategoryInSettingsResponseDto> categories) {
    this.blogName = blogName;
    this.blogBio = blogBio;
    this.blogUrl = blogUrl;
    this.createdAt = createdAt;
    this.thumbUrl = thumbUrl;
    this.thumbOriginName = thumbOriginName;
    this.categories = categories;
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
