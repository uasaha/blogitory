package com.blogitory.blog.category.repository;

import com.blogitory.blog.category.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Category Repository.
 *
 * @author woonseok
 * @since 1.0
 **/
public interface CategoryRepository extends JpaRepository<Category, Long> {
}
