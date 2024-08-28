package com.blogitory.blog.blog.dto.response;

import com.blogitory.blog.category.dto.GetCategoryInSettingsResponseDto;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Get blog with category response dto.
 *
 * @author woonseok
 * @Date 2024-08-02
 * @since 1.0
 **/
@AllArgsConstructor
@Getter
public class GetBlogWithCategoryResponseDto {
  private Long blogNo;
  private String blogName;
  private List<GetCategoryInSettingsResponseDto> categories;

  public void distinct() {
    categories = this.categories.stream().distinct()
            .filter(c -> !c.isDeleted()).toList();
  }
}
