package com.blogitory.blog.posts.dto.request;

import jakarta.validation.constraints.Size;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Temp posts save dto.
 *
 * @author woonseok
 * @since 1.0
 **/
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class SaveTempPostsDto {
  private Long blogNo;

  private String url;

  @Size(min = 1, max = 100)
  private String title;

  private Long categoryNo;

  @Size(max = 300)
  private String summary;

  private String thumbnailUrl;

  @Size(min = 1, max = 50000)
  private String details;

  private List<String> tags;
}
