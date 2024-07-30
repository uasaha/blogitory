package com.blogitory.blog.category.service;

import com.blogitory.blog.category.dto.CategoryCreateResponseDto;

/**
 * Category service.
 *
 * @author woonseok
 * @Date 2024-07-15
 * @since 1.0
 **/
public interface CategoryService {
  /**
   * Create category.
   *
   * @param blogUrl  blog url
   * @param name     category name
   * @param memberNo member no
   * @return new Category
   */
  CategoryCreateResponseDto createCategory(String blogUrl, String name, Integer memberNo);

  /**
   * Modify category.
   *
   * @param categoryNo category no
   * @param name       new name
   * @param memberNo   member no
   */
  void updateCategory(Long categoryNo, String name, Integer memberNo);

  /**
   * Delete category.
   *
   * @param categoryNo category no
   * @param memberNo   member no
   */
  void deleteCategory(Long categoryNo, Integer memberNo);
}
