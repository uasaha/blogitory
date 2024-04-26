package com.blogitory.blog.image.repository;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import com.blogitory.blog.image.entity.Image;
import com.blogitory.blog.image.entity.ImageDummy;
import com.blogitory.blog.imagecategory.entity.ImageCategory;
import com.blogitory.blog.imagecategory.entity.ImageCategoryDummy;
import com.blogitory.blog.imagecategory.repository.ImageCategoryRepository;
import com.blogitory.blog.member.entity.Member;
import com.blogitory.blog.member.entity.MemberDummy;
import com.blogitory.blog.member.repository.MemberRepository;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

/**
 * Image repository test.
 *
 * @author woonseok
 * @since 1.0
 **/
@DataJpaTest
class ImageRepositoryTest {

  @Autowired
  ImageRepository imageRepository;

  @Autowired
  ImageCategoryRepository imageCategoryRepository;

  @Autowired
  MemberRepository memberRepository;

  @Autowired
  EntityManager entityManager;

  @BeforeEach
  void setUp() {
    entityManager.createNativeQuery("ALTER TABLE `member` ALTER COLUMN `member_no` RESTART")
            .executeUpdate();
    entityManager.createNativeQuery("ALTER TABLE `image` ALTER COLUMN `image_no` RESTART")
            .executeUpdate();
    entityManager.createNativeQuery("ALTER TABLE `image_category` ALTER COLUMN `image_category_no` RESTART")
            .executeUpdate();
  }

  @Test
  @DisplayName("이미지 저장")
  void imageSave() {
    ImageCategory imageCategory = ImageCategoryDummy.dummy();
    imageCategory = imageCategoryRepository.save(imageCategory);

    Member member = MemberDummy.dummy();
    member = memberRepository.save(member);

    Image image = ImageDummy.thumbnailDummy(imageCategory, member);
    Image actual = imageRepository.save(image);

    assertAll(
            () -> assertEquals(image.getImageNo(), actual.getImageNo()),
            () -> assertEquals(image.getImageCategory().getImageCategoryNo(), actual.getImageCategory().getImageCategoryNo()),
            () -> assertEquals(image.getMember().getMemberNo(), actual.getMember().getMemberNo()),
            () -> assertNull(image.getPosts()),
            () -> assertNull(image.getBlog()),
            () -> assertEquals(image.getUrl(), actual.getUrl()),
            () -> assertEquals(image.getOriginName(), actual.getOriginName()),
            () -> assertEquals(image.getSaveName(), actual.getSaveName()),
            () -> assertEquals(image.getExtension(), actual.getExtension()),
            () -> assertEquals(image.getSavePath(), actual.getSavePath())
    );
  }
}