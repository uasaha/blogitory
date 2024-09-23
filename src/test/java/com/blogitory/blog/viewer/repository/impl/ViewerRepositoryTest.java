package com.blogitory.blog.viewer.repository.impl;

import static org.junit.jupiter.api.Assertions.*;

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
import com.blogitory.blog.viewer.dto.GetViewerCountResponseDto;
import com.blogitory.blog.viewer.entity.Viewer;
import com.blogitory.blog.viewer.entity.ViewerDummy;
import com.blogitory.blog.viewer.repository.ViewerRepository;
import jakarta.persistence.EntityManager;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

/**
 * Viewer repository test.
 *
 * @author woonseok
 * @Date 2024-09-11
 * @since 1.0
 **/
@Import({JpaConfig.class, QuerydslConfig.class})
@DataJpaTest
class ViewerRepositoryTest {

  @Autowired
  PostsRepository postsRepository;

  @Autowired
  CategoryRepository categoryRepository;

  @Autowired
  MemberRepository memberRepository;

  @Autowired
  BlogRepository blogRepository;

  @Autowired
  ViewerRepository viewerRepository;

  @Autowired
  EntityManager entityManager;

  @AfterEach
  void tearDown() {
    entityManager.createNativeQuery("ALTER TABLE `posts` ALTER COLUMN `posts_no` RESTART")
            .executeUpdate();
    entityManager.createNativeQuery("ALTER TABLE `category` ALTER COLUMN `category_no` RESTART")
            .executeUpdate();
    entityManager.createNativeQuery("ALTER TABLE `member` ALTER COLUMN `member_no` RESTART")
            .executeUpdate();
    entityManager.createNativeQuery("ALTER TABLE `blog` ALTER COLUMN `blog_no` RESTART")
            .executeUpdate();
    entityManager.createNativeQuery("ALTER TABLE `viewer` ALTER COLUMN `viewer_no` RESTART")
            .executeUpdate();
  }

  @Test
  void getCountByPostsUrl() {
    Member member = MemberDummy.dummy();
    member = memberRepository.save(member);
    Blog blog = BlogDummy.dummy(member);
    blog = blogRepository.save(blog);
    Category category = CategoryDummy.dummy(blog);
    category = categoryRepository.save(category);
    Posts posts = PostsDummy.dummy(category);
    posts = postsRepository.save(posts);
    Viewer viewer = ViewerDummy.dummy(posts);
    viewerRepository.save(viewer);

    int result = viewerRepository.getCountByPostsUrl(posts.getUrl());

    assertEquals(0, result);
  }

  @Test
  void findByPostsUrlAndDate() {
    Member member = MemberDummy.dummy();
    member = memberRepository.save(member);
    Blog blog = BlogDummy.dummy(member);
    blog = blogRepository.save(blog);
    Category category = CategoryDummy.dummy(blog);
    category = categoryRepository.save(category);
    Posts posts = PostsDummy.dummy(category);
    posts = postsRepository.save(posts);
    Viewer viewer = ViewerDummy.dummy(posts);
    viewerRepository.save(viewer);

    Optional<Viewer> result = viewerRepository.findByPostsUrlAndDate(posts.getUrl(), viewer.getViewerDate());

    assertTrue(result.isPresent());
    assertEquals(0, result.get().getViewerCnt());
  }

  @Test
  void getCountsByPostUrl() {
    Member member = MemberDummy.dummy();
    member = memberRepository.save(member);
    Blog blog = BlogDummy.dummy(member);
    blog = blogRepository.save(blog);
    Category category = CategoryDummy.dummy(blog);
    category = categoryRepository.save(category);
    Posts posts = PostsDummy.dummy(category);
    posts = postsRepository.save(posts);
    Viewer viewer = ViewerDummy.dummy(posts);
    viewerRepository.save(viewer);

    List<GetViewerCountResponseDto> result =
            viewerRepository.getCountsByPostUrl(posts.getUrl(), LocalDate.now(), LocalDate.now());
    assertEquals(1, result.size());
  }
}