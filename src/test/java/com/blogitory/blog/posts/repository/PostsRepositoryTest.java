package com.blogitory.blog.posts.repository;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.blogitory.blog.blog.entity.Blog;
import com.blogitory.blog.blog.entity.BlogDummy;
import com.blogitory.blog.blog.repository.BlogRepository;
import com.blogitory.blog.category.entity.Category;
import com.blogitory.blog.category.entity.CategoryDummy;
import com.blogitory.blog.category.repository.CategoryRepository;
import com.blogitory.blog.member.entity.Member;
import com.blogitory.blog.member.entity.MemberDummy;
import com.blogitory.blog.member.repository.MemberRepository;
import com.blogitory.blog.posts.entity.Posts;
import com.blogitory.blog.posts.entity.PostsDummy;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

/**
 * Posts repository test.
 *
 * @author woonseok
 * @since 1.0
 */
@DataJpaTest
class PostsRepositoryTest {

  /**
   * The Posts repository.
   */
  @Autowired
  PostsRepository postsRepository;

  /**
   * The Category repository.
   */
  @Autowired
  CategoryRepository categoryRepository;

  /**
   * The Member repository.
   */
  @Autowired
  MemberRepository memberRepository;

  /**
   * The Blog repository.
   */
  @Autowired
  BlogRepository blogRepository;

  /**
   * The Entity manager.
   */
  @Autowired
  EntityManager entityManager;

  /**
   * Sets up.
   */
  @BeforeEach
  void setUp() {
  }

  /**
   * Teardown.
   */
  @AfterEach
  void teardown() {
    entityManager.createNativeQuery("ALTER TABLE `posts` ALTER COLUMN `posts_no` RESTART")
            .executeUpdate();
    entityManager.createNativeQuery("ALTER TABLE `category` ALTER COLUMN `category_no` RESTART")
            .executeUpdate();
    entityManager.createNativeQuery("ALTER TABLE `member` ALTER COLUMN `member_no` RESTART")
            .executeUpdate();
    entityManager.createNativeQuery("ALTER TABLE `blog` ALTER COLUMN `blog_no` RESTART")
            .executeUpdate();
  }

  /**
   * Posts save.
   */
  @Test
  @DisplayName("글 저장")
  void postsSave() {
    Member member = MemberDummy.dummy();
    member = memberRepository.save(member);

    Blog blog = BlogDummy.dummy(member);
    blog = blogRepository.save(blog);

    Category category = CategoryDummy.dummy(blog);
    category = categoryRepository.save(category);

    Posts posts = PostsDummy.dummy(category);
    Posts actual = postsRepository.save(posts);

    assertAll(
            () -> assertEquals(posts.getPostsNo(), actual.getPostsNo()),
            () -> assertEquals(posts.getCategory().getCategoryNo(), actual.getCategory().getCategoryNo()),
            () -> assertEquals(posts.getSubject(), actual.getSubject()),
            () -> assertEquals(posts.getUrl(), actual.getUrl()),
            () -> assertEquals(posts.getSummary(), actual.getSummary()),
            () -> assertEquals(posts.getThumbnail(), actual.getThumbnail()),
            () -> assertEquals(posts.getDetail(), actual.getDetail()),
            () -> assertEquals(posts.getUpdatedAt(), actual.getUpdatedAt()),
            () -> assertEquals(posts.getCreatedAt(), actual.getCreatedAt()),
            () -> assertEquals(posts.isOpen(), actual.isOpen()),
            () -> assertEquals(posts.getHeartAmount(), actual.getHeartAmount()),
            () -> assertEquals(posts.isDeleted(), actual.isDeleted())
    );
  }
}