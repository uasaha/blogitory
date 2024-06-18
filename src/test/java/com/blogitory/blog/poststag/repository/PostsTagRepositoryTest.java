package com.blogitory.blog.poststag.repository;

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
import com.blogitory.blog.posts.repository.PostsRepository;
import com.blogitory.blog.poststag.entity.PostsTag;
import com.blogitory.blog.poststag.entity.PostsTagDummy;
import com.blogitory.blog.tag.entity.Tag;
import com.blogitory.blog.tag.entity.TagDummy;
import com.blogitory.blog.tag.repository.TagRepository;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

/**
 * PostsTag repository test.
 *
 * @author woonseok
 * @since 1.0
 */
@DataJpaTest
class PostsTagRepositoryTest {

  /**
   * The Posts tag repository.
   */
  @Autowired
  PostsTagRepository postsTagRepository;

  /**
   * The Tag repository.
   */
  @Autowired
  TagRepository tagRepository;

  /**
   * The Posts repository.
   */
  @Autowired
  PostsRepository postsRepository;

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
   * The Category repository.
   */
  @Autowired
  CategoryRepository categoryRepository;

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
    entityManager.createNativeQuery("ALTER TABLE `posts` ALTER COLUMN `posts_no` RESTART")
            .executeUpdate();
    entityManager.createNativeQuery("ALTER TABLE `category` ALTER COLUMN `category_no` RESTART")
            .executeUpdate();
    entityManager.createNativeQuery("ALTER TABLE `tag` ALTER COLUMN `tag_no` RESTART")
            .executeUpdate();
    entityManager.createNativeQuery("ALTER TABLE `posts_tag` ALTER COLUMN `posts_tag_no` RESTART")
            .executeUpdate();
  }

  /**
   * Posts tag save.
   */
  @Test
  @DisplayName("PostsTag 저장")
  void PostsTagSave() {
    Tag tag = TagDummy.dummy();
    tag = tagRepository.save(tag);

    Member member = MemberDummy.dummy();
    member = memberRepository.save(member);

    Blog blog = BlogDummy.dummy(member);
    blog = blogRepository.save(blog);

    Category category = CategoryDummy.dummy(blog);
    category = categoryRepository.save(category);

    Posts posts = PostsDummy.dummy(category);
    posts = postsRepository.save(posts);

    PostsTag postsTag = PostsTagDummy.dummy(tag, posts, blog);
    PostsTag actual = postsTagRepository.save(postsTag);

    assertAll(
            () -> assertEquals(postsTag.getPostsTagNo(), actual.getPostsTagNo()),
            () -> assertEquals(postsTag.getTag().getTagNo(), actual.getTag().getTagNo()),
            () -> assertEquals(postsTag.getPosts().getPostsNo(), actual.getPosts().getPostsNo()),
            () -> assertEquals(postsTag.getBlog(), actual.getBlog())
    );
  }
}