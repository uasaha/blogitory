package com.blogitory.blog.imagecategory.repository;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.blogitory.blog.imagecategory.entity.ImageCategory;
import com.blogitory.blog.imagecategory.entity.ImageCategoryDummy;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

/**
 * ImageCategory repository.
 *
 * @author woonseok
 * @since 1.0
 **/
@DataJpaTest
class ImageCategoryRepositoryTest {

  @Autowired
  ImageCategoryRepository imageCategoryRepository;

  @Autowired
  EntityManager entityManager;

  @BeforeEach
  void setUp() {
  }

  @AfterEach
  void teardown() {
    entityManager.createNativeQuery("ALTER TABLE `image_category` ALTER COLUMN `image_category_no` RESTART")
            .executeUpdate();
  }

  @Test
  @DisplayName("이미지_카테고리 저장")
  void imageCategorySave() {
    ImageCategory imageCategory = ImageCategoryDummy.dummy();
    ImageCategory actual = imageCategoryRepository.save(imageCategory);

    assertAll(
            () -> assertEquals(imageCategory.getImageCategoryNo(), actual.getImageCategoryNo()),
            () -> assertEquals(imageCategory.getName(), actual.getName())
    );
  }
}