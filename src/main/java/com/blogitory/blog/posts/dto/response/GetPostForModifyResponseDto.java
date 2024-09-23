package com.blogitory.blog.posts.dto.response;

import com.blogitory.blog.tag.dto.GetTagResponseDto;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Dto for modify post.
 *
 * @author woonseok
 * @since 1.0
 **/
@NoArgsConstructor
@Getter
public class GetPostForModifyResponseDto {
  private String blogName;
  private String categoryName;
  private String title;
  private String postUrl;
  private String thumbnailUrl;
  private String summary;
  private String detail;
  private List<String> tags;

  public void tags(List<GetTagResponseDto> tags) {
    this.tags = tags.stream().map(GetTagResponseDto::getTagName).toList();
  }
}
