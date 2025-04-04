package com.blogitory.blog.posts.dto.request;

import jakarta.validation.constraints.Size;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * ModifyPostsRequestDto.
 *
 * @author woonseok
 * @since 1.0
 **/
@NoArgsConstructor
@Getter
public class ModifyPostsRequestDto {
  @Size(min = 1, max = 100)
  private String title;

  @Size(max = 300)
  private String summary;

  @Size(min = 1, max = 50000)
  private String content;

  private String thumb;

  private List<String> tags;
}
