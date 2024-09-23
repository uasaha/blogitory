package com.blogitory.blog.category.service;

import com.blogitory.blog.category.dto.CreateCategoryResponseDto;

/**
 * Category service.
 *
 * @author woonseok
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
  CreateCategoryResponseDto createCategory(String blogUrl, String name, Integer memberNo);

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
