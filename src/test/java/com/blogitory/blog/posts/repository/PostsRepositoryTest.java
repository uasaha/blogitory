package com.blogitory.blog.posts.repository;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
import com.blogitory.blog.posts.dto.response.GetPopularPostResponseDto;
import com.blogitory.blog.posts.dto.response.GetPostForModifyResponseDto;
import com.blogitory.blog.posts.dto.response.GetPostResponseDto;
import com.blogitory.blog.posts.dto.response.GetRecentPostResponseDto;
import com.blogitory.blog.posts.entity.Posts;
import com.blogitory.blog.posts.entity.PostsDummy;
import jakarta.persistence.EntityManager;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

/**
 * Posts repository test.
 *
 * @author woonseok
 * @since 1.0
 */
@Import({JpaConfig.class, QuerydslConfig.class})
@DataJpaTest
class PostsRepositoryTest {

  @Autowired
  PostsRepository postsRepository;

  @Autowired
  CategoryRepository categoryRepository;

  @Autowired
  MemberRepository memberRepository;

  @Autowired
  BlogRepository blogRepository;

  @Autowired
  EntityManager entityManager;

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
            () -> assertEquals(posts.getViews(), actual.getViews()),
            () -> assertEquals(posts.getThumbnail(), actual.getThumbnail()),
            () -> assertEquals(posts.getDetail(), actual.getDetail()),
            () -> assertEquals(posts.getUpdatedAt(), actual.getUpdatedAt()),
            () -> assertNotNull(actual.getCreatedAt()),
            () -> assertEquals(posts.isOpen(), actual.isOpen()),
            () -> assertEquals(posts.isDeleted(), actual.isDeleted())
    );
  }

  @Test
  @DisplayName("글 조회")
  void getPosts() {
    Member member = MemberDummy.dummy();
    member = memberRepository.save(member);

    Blog blog = BlogDummy.dummy(member);
    blog = blogRepository.save(blog);

    Category category = CategoryDummy.dummy(blog);
    category = categoryRepository.save(category);

    Posts posts = PostsDummy.dummy(category);
    posts = postsRepository.save(posts);

    Optional<GetPostResponseDto> responseDto =
            postsRepository.getPostByPostUrl(posts.getUrl());

    assertTrue(responseDto.isPresent());
    GetPostResponseDto actual = responseDto.get();
    assertNotNull(actual);
    assertEquals(member.getUsername(), actual.getUsername());
    assertEquals(member.getName(), actual.getMemberName());
    assertEquals(blog.getName(), actual.getBlogName());
    assertEquals(blog.getUrlName(), actual.getBlogUrl());
    assertEquals(category.getCategoryNo(), actual.getCategoryNo());
    assertEquals(category.getName(), actual.getCategoryName());
    assertEquals(posts.getUrl(), actual.getPostUrl());
    assertEquals(posts.getSubject(), actual.getSubject());
    assertEquals(posts.getSummary(), actual.getSummary());
    assertEquals(posts.getViews(), actual.getViews());
    assertEquals(posts.getDetail(), actual.getDetail());
  }

  @Test
  @DisplayName("글 수정을 위한 정보 조회")
  void getPostForModifyByUrl() {
    Member member = MemberDummy.dummy();
    member = memberRepository.save(member);
    Blog blog = BlogDummy.dummy(member);
    blog = blogRepository.save(blog);
    Category category = CategoryDummy.dummy(blog);
    category = categoryRepository.save(category);
    Posts posts = PostsDummy.dummy(category);
    posts = postsRepository.save(posts);

    Optional<GetPostForModifyResponseDto> postOptional =
            postsRepository.getPostForModifyByUrl(member.getMemberNo(), posts.getUrl());

    assertTrue(postOptional.isPresent());

    GetPostForModifyResponseDto actual = postOptional.get();
    assertEquals(blog.getName(), actual.getBlogName());
    assertEquals(category.getName(), actual.getCategoryName());
    assertEquals(posts.getUrl(), actual.getPostUrl());
    assertEquals(posts.getThumbnail(), actual.getThumbnailUrl());
    assertEquals(posts.getSummary(), actual.getSummary());
    assertEquals(posts.getDetail(), actual.getDetail());
  }

  @Test
  @DisplayName("최근 글 조회")
  void getRecentPosts() {
    Member member = MemberDummy.dummy();
    member = memberRepository.save(member);
    Blog blog = BlogDummy.dummy(member);
    blog = blogRepository.save(blog);
    Category category = CategoryDummy.dummy(blog);
    category = categoryRepository.save(category);
    Posts posts = PostsDummy.dummy(category);
    posts = postsRepository.save(posts);

    Pageable pageable = PageRequest.of(0, 10);

    Page<GetRecentPostResponseDto> page = postsRepository.getRecentPosts(pageable);

    assertFalse(page.hasPrevious());
    assertFalse(page.hasNext());
    assertEquals(1L, page.getTotalElements());
    assertEquals(posts.getSubject(), page.getContent().getFirst().getTitle());
  }

  @Test
  @DisplayName("회원 최근 글 조회")
  void getRecentPostsByUsername() {
    Member member = MemberDummy.dummy();
    member = memberRepository.save(member);
    Blog blog = BlogDummy.dummy(member);
    blog = blogRepository.save(blog);
    Category category = CategoryDummy.dummy(blog);
    category = categoryRepository.save(category);
    Posts posts = PostsDummy.dummy(category);
    posts = postsRepository.save(posts);

    Pageable pageable = PageRequest.of(0, 10);

    List<GetRecentPostResponseDto> list = postsRepository.getRecentPostByUsername(pageable, member.getUsername());
    GetRecentPostResponseDto actual = list.getFirst();

    assertEquals(posts.getSubject(), actual.getTitle());
  }

  @Test
  @DisplayName("회원 최근 글 조회")
  void getRecentPostsByBlog() {
    Member member = MemberDummy.dummy();
    member = memberRepository.save(member);
    Blog blog = BlogDummy.dummy(member);
    blog = blogRepository.save(blog);
    Category category = CategoryDummy.dummy(blog);
    category = categoryRepository.save(category);
    Posts posts = PostsDummy.dummy(category);
    posts = postsRepository.save(posts);

    Pageable pageable = PageRequest.of(0, 10);

    List<GetRecentPostResponseDto> list = postsRepository.getRecentPostByBlog(pageable, blog.getUrlName()).getContent();
    GetRecentPostResponseDto actual = list.getFirst();

    assertEquals(posts.getSubject(), actual.getTitle());
  }

  @Test
  @DisplayName("인기글 조회")
  void getPopularPostsByBlog() {
    Member member = MemberDummy.dummy();
    member = memberRepository.save(member);
    Blog blog = BlogDummy.dummy(member);
    blog = blogRepository.save(blog);
    Category category = CategoryDummy.dummy(blog);
    category = categoryRepository.save(category);
    Posts posts = PostsDummy.dummy(category);
    posts = postsRepository.save(posts);

    List<GetPopularPostResponseDto> responseList =
            postsRepository.getPopularPostsByBlog(blog.getUrlName());

    assertFalse(responseList.isEmpty());

    GetPopularPostResponseDto actual = responseList.getFirst();

    assertEquals(posts.getSubject(), actual.getTitle());
  }
}