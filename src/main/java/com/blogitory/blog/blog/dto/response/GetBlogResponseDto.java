package com.blogitory.blog.blog.dto.response;

import com.blogitory.blog.category.dto.GetCategoryResponseDto;
import com.blogitory.blog.tag.dto.GetTagResponseDto;
import java.util.List;
import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Get blog response dto.
 *
 * @author woonseok
 * @Date 2024-07-31
 * @since 1.0
 **/
@AllArgsConstructor
@Getter
public class GetBlogResponseDto {
  private String blogThumbUrl;
  private String blogThumbOriginName;
  private String blogUrl;
  private String blogName;
  private String name;
  private String username;
  private String blogBio;
  private List<GetCategoryResponseDto> categories;
  private List<GetTagResponseDto> tags;

  /**
   * distinct category, tag.
   */
  public void distinct() {
    categories = categories.stream().filter(c -> !c.isDeleted()).distinct().toList();
    tags = tags.stream().filter(t -> Objects.nonNull(t.getTagName()))
            .distinct().toList();
  }
}
