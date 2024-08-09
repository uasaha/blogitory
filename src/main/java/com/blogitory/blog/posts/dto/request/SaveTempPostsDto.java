package com.blogitory.blog.posts.dto.request;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Temp posts save dto.
 *
 * @author woonseok
 * @Date 2024-08-01
 * @since 1.0
 **/
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class SaveTempPostsDto {
  private Long blogNo;
  @Setter
  private Integer memberNo;

  @Size(min = 1, max = 100)
  @Pattern(regexp = "^[ㄱ-ㅎ가-힣a-zA-Z0-9\\s]+$")
  private String title;

  private Long categoryNo;

  @Size(max = 100)
  private String url;

  @Size(max = 300)
  private String summary;
  private String thumbnailUrl;

  @Size(min = 1, max = 50000)
  private String details;
  private List<String> tags;
}
