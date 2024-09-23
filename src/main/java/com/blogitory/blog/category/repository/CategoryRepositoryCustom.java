package com.blogitory.blog.category.repository;

import com.blogitory.blog.category.dto.GetCategoryResponseDto;
import java.util.List;
import org.springframework.data.repository.NoRepositoryBean;

/**
 * Custom category repository.
 *
 * @author woonseok
 * @since 1.0
 **/
@NoRepositoryBean
public interface CategoryRepositoryCustom {

  /**
   * Get categories by blog.
   *
   * @param blogUrl blog url
   * @return categories
   */
  List<GetCategoryResponseDto> getCategoriesByBlog(String blogUrl);
}
