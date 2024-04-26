package com.blogitory.blog.category.repository;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.blogitory.blog.blog.entity.Blog;
import com.blogitory.blog.blog.entity.BlogDummy;
import com.blogitory.blog.blog.repository.BlogRepository;
import com.blogitory.blog.category.entity.Category;
import com.blogitory.blog.category.entity.CategoryDummy;
import com.blogitory.blog.member.entity.Member;
import com.blogitory.blog.member.entity.MemberDummy;
import com.blogitory.blog.member.repository.MemberRepository;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

/**
 * CategoryRepository test.
 *
 * @author woonseok
 * @since 1.0
 **/
@DataJpaTest
class CategoryRepositoryTest {

  @Autowired
  MemberRepository memberRepository;

  @Autowired
  BlogRepository blogRepository;

  @Autowired
  CategoryRepository categoryRepository;

  @Autowired
  EntityManager entityManager;

  @BeforeEach
  void setUp() {
  }

  @AfterEach
  void teardown() {
    entityManager.createNativeQuery("ALTER TABLE `category` ALTER COLUMN `category_no` RESTART")
            .executeUpdate();
    entityManager.createNativeQuery("ALTER TABLE `member` ALTER COLUMN `member_no` RESTART")
            .executeUpdate();
    entityManager.createNativeQuery("ALTER TABLE `blog` ALTER COLUMN `blog_no` RESTART")
            .executeUpdate();
  }

  @Test
  @DisplayName("카테고리 저장")
  void save() {
    Member member = MemberDummy.dummy();
    member = memberRepository.save(member);

    Blog blog = BlogDummy.dummy(member);
    blog = blogRepository.save(blog);

    Category category = CategoryDummy.dummy(blog);
    Category actual = categoryRepository.save(category);

    assertAll(
            () -> assertEquals(category.getCategoryNo(), actual.getCategoryNo()),
            () -> assertEquals(category.getBlog().getName(), actual.getBlog().getName()),
            () -> assertEquals(category.getName(), actual.getName()),
            () -> assertEquals(category.isDeleted(), actual.isDeleted())
    );
  }
}