package com.blogitory.blog.blog.repository;


import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import com.blogitory.blog.blog.dto.BlogListInSettingsResponseDto;
import com.blogitory.blog.blog.entity.Blog;
import com.blogitory.blog.blog.entity.BlogDummy;
import com.blogitory.blog.image.entity.Image;
import com.blogitory.blog.image.entity.ImageDummy;
import com.blogitory.blog.image.repository.ImageRepository;
import com.blogitory.blog.imagecategory.entity.ImageCategory;
import com.blogitory.blog.imagecategory.entity.ImageCategoryDummy;
import com.blogitory.blog.imagecategory.repository.ImageCategoryRepository;
import com.blogitory.blog.member.entity.Member;
import com.blogitory.blog.member.entity.MemberDummy;
import com.blogitory.blog.member.repository.MemberRepository;
import jakarta.persistence.EntityManager;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

/**
 * Blog repository test.
 *
 * @author woonseok
 * @since 1.0
 */
@DataJpaTest
class BlogRepositoryTest {
  /**
   * The Blog repository.
   */
  @Autowired
  BlogRepository blogRepository;

  /**
   * The Member repository.
   */
  @Autowired
  MemberRepository memberRepository;

  /**
   * The Image category repository.
   */
  @Autowired
  ImageCategoryRepository imageCategoryRepository;

  /**
   * The Image repository.
   */
  @Autowired
  ImageRepository imageRepository;

  /**
   * The Entity manager.
   */
  @Autowired
  EntityManager entityManager;

  /**
   * Teardown.
   */
  @AfterEach
  void teardown() {
    entityManager.createNativeQuery("ALTER TABLE `member` ALTER COLUMN `member_no` RESTART")
            .executeUpdate();
    entityManager.createNativeQuery("ALTER TABLE `blog` ALTER COLUMN `blog_no` RESTART")
            .executeUpdate();
    entityManager.createNativeQuery("ALTER TABLE `image_category` ALTER COLUMN `image_category_no` RESTART")
            .executeUpdate();
    entityManager.createNativeQuery("ALTER TABLE `image` ALTER COLUMN `image_no` RESTART")
            .executeUpdate();
  }

  /**
   * Blog save.
   */
  @Test
  @DisplayName("블로그 저장")
  void blogSave() {
    Member member = MemberDummy.dummy();
    member = memberRepository.save(member);

    Blog blog = BlogDummy.dummy(member);
    Blog actual = blogRepository.save(blog);

    assertAll(
            () -> assertEquals(blog.getBlogNo(), actual.getBlogNo()),
            () -> assertEquals(blog.getMember().getMemberNo(), actual.getMember().getMemberNo()),
            () -> assertEquals(blog.getBio(), actual.getBio()),
            () -> assertEquals(blog.getUrlName(), actual.getUrlName()),
            () -> assertEquals(blog.getBackground(), actual.getBackground()),
            () -> assertEquals(blog.getIntro(), actual.getIntro()),
            () -> assertEquals(blog.getTheme(), actual.getTheme()),
            () -> assertEquals(blog.isDeleted(), actual.isDeleted())
    );
  }

  /**
   * Gets blog list by member no.
   */
  @Test
  @DisplayName("회원번호로 블로그 조회")
  void getBlogListByMemberNo() {
    Member member = MemberDummy.dummy();
    member = memberRepository.save(member);

    Blog blog = BlogDummy.dummy(member);
    blog = blogRepository.save(blog);

    ImageCategory imageCategory = ImageCategoryDummy.dummy();
    imageCategory = imageCategoryRepository.save(imageCategory);

    Image image = ImageDummy.blogDummy(imageCategory, blog);
    image = imageRepository.save(image);

    List<BlogListInSettingsResponseDto> actual =
            blogRepository.getBlogListByMemberNo(member.getMemberNo());

    BlogListInSettingsResponseDto actualGet = actual.get(0);

    Blog expect = blog;
    Image expectImage = image;

    assertAll(
            () -> assertEquals(expect.getBio(), actualGet.getBlogBio()),
            () -> assertEquals(expect.getUrlName(), actualGet.getBlogUrl()),
            () -> assertEquals(expect.getIntro(), actualGet.getBlogIntro()),
            () -> assertEquals(expect.getCreatedAt(), actualGet.getCreatedAt()),
            () -> assertFalse(actualGet.isThumbIsNull()),
            () -> assertEquals(expectImage.getUrl(), actualGet.getThumbUrl()),
            () -> assertEquals(expectImage.getOriginName(), actualGet.getThumbOriginName())
    );
  }
}