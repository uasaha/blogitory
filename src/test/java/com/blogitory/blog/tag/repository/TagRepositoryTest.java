package com.blogitory.blog.tag.repository;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import com.blogitory.blog.blog.entity.Blog;
import com.blogitory.blog.blog.entity.BlogDummy;
import com.blogitory.blog.blog.repository.BlogRepository;
import com.blogitory.blog.category.entity.Category;
import com.blogitory.blog.category.entity.CategoryDummy;
import com.blogitory.blog.category.repository.CategoryRepository;
import com.blogitory.blog.commons.config.JpaConfig;
import com.blogitory.blog.commons.config.QuerydslConfig;
import com.blogitory.blog.member.entity.Member;
import com.blogitory.blog.member.entity.MemberDummy;
import com.blogitory.blog.member.repository.MemberRepository;
import com.blogitory.blog.posts.entity.Posts;
import com.blogitory.blog.posts.entity.PostsDummy;
import com.blogitory.blog.posts.repository.PostsRepository;
import com.blogitory.blog.poststag.entity.PostsTag;
import com.blogitory.blog.poststag.entity.PostsTagDummy;
import com.blogitory.blog.poststag.repository.PostsTagRepository;
import com.blogitory.blog.tag.dto.GetTagResponseDto;
import com.blogitory.blog.tag.entity.Tag;
import com.blogitory.blog.tag.entity.TagDummy;
import jakarta.persistence.EntityManager;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

/**
 * Tag repository test.
 *
 * @author woonseok
 * @since 1.0
 */
@Import({JpaConfig.class, QuerydslConfig.class})
@DataJpaTest
class TagRepositoryTest {

  @Autowired
  TagRepository tagRepository;

  @Autowired
  MemberRepository memberRepository;

  @Autowired
  BlogRepository blogRepository;

  @Autowired
  CategoryRepository categoryRepository;

  @Autowired
  PostsRepository postsRepository;

  @Autowired
  PostsTagRepository postsTagRepository;

  @Autowired
  EntityManager entityManager;

  @BeforeEach
  void setUp() {
  }

  @AfterEach
  void tearDown() {
    entityManager.createNativeQuery("ALTER TABLE `member` ALTER COLUMN `member_no` RESTART")
            .executeUpdate();
    entityManager.createNativeQuery("ALTER TABLE `blog` ALTER COLUMN `blog_no` RESTART")
            .executeUpdate();
    entityManager.createNativeQuery("ALTER TABLE `category` ALTER COLUMN `category_no` RESTART")
            .executeUpdate();
    entityManager.createNativeQuery("ALTER TABLE `posts` ALTER COLUMN `posts_no` RESTART")
            .executeUpdate();
    entityManager.createNativeQuery("ALTER TABLE `posts_tag` ALTER COLUMN `posts_tag_no` RESTART")
            .executeUpdate();
    entityManager.createNativeQuery("ALTER TABLE `tag` ALTER COLUMN `tag_no` RESTART")
            .executeUpdate();
  }

  @Test
  @DisplayName("태그 저장")
  void tagSave() {
    Tag tag = TagDummy.dummy();
    Tag actual = tagRepository.save(tag);

    assertAll(
            () -> assertEquals(tag.getTagNo(), actual.getTagNo()),
            () -> assertEquals(tag.getName(), actual.getName()),
            () -> assertEquals(tag.isDeleted(), actual.isDeleted())
    );
  }

  @Test
  @DisplayName("게시물 태그 조회")
  void getTag() {
    Member member = MemberDummy.dummy();
    member = memberRepository.save(member);

    Blog blog = BlogDummy.dummy(member);
    blog = blogRepository.save(blog);

    Category category = CategoryDummy.dummy(blog);
    category = categoryRepository.save(category);

    Posts posts = PostsDummy.dummy(category);
    posts = postsRepository.save(posts);

    Tag tag = TagDummy.dummy();
    tag = tagRepository.save(tag);

    PostsTag postsTag = PostsTagDummy.dummy(tag, posts, blog);
    postsTag = postsTagRepository.save(postsTag);

    List<GetTagResponseDto> tagList = tagRepository.getTagListByPost(posts.getUrl());

    assertFalse(tagList.isEmpty());

    GetTagResponseDto tagDto = tagList.getFirst();

    assertEquals(tag.getName(), tagDto.getTagName());
  }
}