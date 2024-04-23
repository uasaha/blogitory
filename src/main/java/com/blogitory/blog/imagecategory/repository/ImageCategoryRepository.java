package com.blogitory.blog.imagecategory.repository;

import com.blogitory.blog.imagecategory.entity.ImageCategory;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Image Category JPA Repository.
 *
 * @author woonseok
 * @since 1.0
 **/
public interface ImageCategoryRepository extends JpaRepository<ImageCategory, Integer> {
  Optional<ImageCategory> findByName(String name);
}
