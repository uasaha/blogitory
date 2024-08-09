package com.blogitory.blog.blog.dto.response;

import com.blogitory.blog.category.dto.GetCategoryResponseDto;
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
  private List<GetCategoryResponseDto> categories;
}
