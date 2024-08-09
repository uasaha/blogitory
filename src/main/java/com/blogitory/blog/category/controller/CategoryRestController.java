package com.blogitory.blog.category.controller;

import com.blogitory.blog.category.dto.CreateCategoryResponseDto;
import com.blogitory.blog.category.service.CategoryService;
import com.blogitory.blog.commons.annotaion.RoleUser;
import com.blogitory.blog.security.util.SecurityUtils;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Category rest controller.
 *
 * @author woonseok
 * @Date 2024-07-15
 * @since 1.0
 **/
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/categories")
public class CategoryRestController {
  private final CategoryService categoryService;

  /**
   * Create new category.
   *
   * @param name    category name
   * @param blogUrl blog url
   * @return new category(200)
   */
  @RoleUser
  @PostMapping
  public ResponseEntity<CreateCategoryResponseDto> createCategory(
          @Valid @RequestParam
          @Size(min = 1, max = 30) @Pattern(regexp = "^[ㄱ-ㅎ가-힣a-zA-Z0-9-]+$")
          String name,
          @RequestParam String blogUrl) {
    Integer memberNo = SecurityUtils.getCurrentUserNo();
    CreateCategoryResponseDto responseDto =
            categoryService.createCategory(blogUrl, name, memberNo);

    return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
  }

  /**
   * Modify category.
   *
   * @param categoryNo category no
   * @param name       category name
   * @return 204
   */
  @RoleUser
  @PutMapping("/{categoryNo}")
  public ResponseEntity<Void> updateCategory(@PathVariable Long categoryNo,
                                             @Valid @RequestParam
                                             @Size(min = 1, max = 30)
                                             @Pattern(regexp = "^[ㄱ-ㅎ가-힣a-zA-Z0-9_-]+$")
                                             String name) {
    Integer memberNo = SecurityUtils.getCurrentUserNo();
    categoryService.updateCategory(categoryNo, name, memberNo);

    return ResponseEntity.noContent().build();
  }

  /**
   * Delete category.
   *
   * @param categoryNo category no
   * @return 204
   */
  @RoleUser
  @DeleteMapping("/{categoryNo}")
  public ResponseEntity<Void> deleteCategory(@PathVariable Long categoryNo) {
    Integer memberNo = SecurityUtils.getCurrentUserNo();
    categoryService.deleteCategory(categoryNo, memberNo);

    return ResponseEntity.noContent().build();
  }
}
