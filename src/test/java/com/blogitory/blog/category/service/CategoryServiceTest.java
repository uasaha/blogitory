package com.blogitory.blog.category.service;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.blogitory.blog.blog.entity.Blog;
import com.blogitory.blog.blog.entity.BlogDummy;
import com.blogitory.blog.blog.repository.BlogRepository;
import com.blogitory.blog.category.dto.CreateCategoryResponseDto;
import com.blogitory.blog.category.entity.Category;
import com.blogitory.blog.category.entity.CategoryDummy;
import com.blogitory.blog.category.exception.DuplicateCategoryException;
import com.blogitory.blog.category.repository.CategoryRepository;
import com.blogitory.blog.category.service.impl.CategoryServiceImpl;
import com.blogitory.blog.commons.exception.NotFoundException;
import com.blogitory.blog.member.entity.Member;
import com.blogitory.blog.member.entity.MemberDummy;
import com.blogitory.blog.posts.entity.Posts;
import com.blogitory.blog.posts.entity.PostsDummy;
import com.blogitory.blog.security.exception.AuthorizationException;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.util.ReflectionTestUtils;

/**
 * Category service test.
 *
 * @author woonseok
 * @Date 2024-07-29
 * @since 1.0
 **/
class CategoryServiceTest {
  BlogRepository blogRepository;
  CategoryRepository categoryRepository;
  CategoryService categoryService;

  /**
   * Sets up.
   */
  @BeforeEach
  void setUp() {
    blogRepository = mock(BlogRepository.class);
    categoryRepository = mock(CategoryRepository.class);

    categoryService = new CategoryServiceImpl(blogRepository, categoryRepository);
  }

  @WithMockUser("1")
  @Test
  @DisplayName("카테고리 생성")
  void createCategory() {
    Member member = MemberDummy.dummy();
    Blog blog = BlogDummy.dummy(member);
    Category category = CategoryDummy.dummy(blog);

    when(blogRepository.findBlogByUrlName(blog.getUrlName()))
            .thenReturn(Optional.of(blog));
    when(categoryRepository.save(any())).thenReturn(category);

    CreateCategoryResponseDto responseDto =
            categoryService.createCategory(blog.getUrlName(), category.getName(), 1);

    verify(categoryRepository, times(1)).save(any());

    assertEquals(category.getCategoryNo(), responseDto.getCategoryNo());
    assertEquals(category.getName(), responseDto.getName());
  }

  @WithMockUser("1")
  @Test
  @DisplayName("카테고리 생성 실패 - 없는 블로그")
  void createCategoryFailed() {
    Member member = MemberDummy.dummy();
    Blog blog = BlogDummy.dummy(member);
    Category category = CategoryDummy.dummy(blog);
    ReflectionTestUtils.setField(blog, "categories", List.of(category));

    when(blogRepository.findBlogByUrlName(blog.getUrlName()))
            .thenReturn(Optional.empty());
    when(categoryRepository.save(any())).thenReturn(category);

    assertThatThrownBy(() -> categoryService.createCategory("url",
                    "category", 1)).isInstanceOf(NotFoundException.class);
  }

  @WithMockUser("1")
  @Test
  @DisplayName("카테고리 생성 실패 - 다른 유저")
  void createCategoryFailedAnother() {
    Member member = MemberDummy.dummy();
    Blog blog = BlogDummy.dummy(member);
    ReflectionTestUtils.setField(member, "memberNo", 5);

    when(blogRepository.findBlogByUrlName(blog.getUrlName()))
            .thenReturn(Optional.of(blog));

    assertThrows(AuthorizationException.class,
            () -> categoryService.createCategory("urlName",
                    "category", 1));
  }

  @WithMockUser("1")
  @Test
  @DisplayName("카테고리 생성 실패 - 중복")
  void createCategoryFailedDuplicated() {
    Member member = MemberDummy.dummy();
    Blog blog = BlogDummy.dummy(member);
    Category category = CategoryDummy.dummy(blog);
    ReflectionTestUtils.setField(blog, "categories", List.of(category));


    when(blogRepository.findBlogByUrlName(blog.getUrlName()))
            .thenReturn(Optional.of(blog));

    assertThrows(DuplicateCategoryException.class,
            () -> categoryService.createCategory("urlName",
                    "DummyCategory", 1));
  }

  @WithMockUser("1")
  @Test
  @DisplayName("카테고리 수정 성공")
  void updateCategory() {
    Member member = MemberDummy.dummy();
    Blog blog = BlogDummy.dummy(member);
    Category category = CategoryDummy.dummy(blog);

    when(categoryRepository.findById(category.getCategoryNo()))
            .thenReturn(Optional.of(category));

    categoryService.updateCategory(category.getCategoryNo(), "newOne", 1);

    assertEquals("newOne", category.getName());
  }

  @WithMockUser("1")
  @Test
  @DisplayName("카테고리 수정 실패 - 없는 카테고리")
  void updateCategoryFailedNoCategory() {
    Member member = MemberDummy.dummy();
    Blog blog = BlogDummy.dummy(member);
    Category category = CategoryDummy.dummy(blog);

    when(categoryRepository.findById(category.getCategoryNo()))
            .thenReturn(Optional.empty());

    assertThrows(NotFoundException.class,
            () -> categoryService.updateCategory(2L,
                    "DummyCategory", 1));
  }

  @WithMockUser("1")
  @Test
  @DisplayName("카테고리 수정 실패 - 회원 미일치")
  void updateCategoryFailedNoMember() {
    Member member = MemberDummy.dummy();
    Blog blog = BlogDummy.dummy(member);
    Category category = CategoryDummy.dummy(blog);

    when(categoryRepository.findById(category.getCategoryNo()))
            .thenReturn(Optional.of(category));

    assertThrows(AuthorizationException.class,
            () -> categoryService.updateCategory(1L,
                    "DummyCategory", 5));
  }

  @WithMockUser("1")
  @Test
  @DisplayName("카테고리 수정 실패 - 중복")
  void updateCategoryFailedDuplicated() {
    Member member = MemberDummy.dummy();
    Blog blog = BlogDummy.dummy(member);
    Category category = CategoryDummy.dummy(blog);
    ReflectionTestUtils.setField(blog, "categories", List.of(category));


    when(categoryRepository.findById(category.getCategoryNo()))
            .thenReturn(Optional.of(category));

    assertThrows(DuplicateCategoryException.class,
            () -> categoryService.updateCategory(1L,
                    "DummyCategory", 1));
  }

  @WithMockUser("1")
  @Test
  @DisplayName("카테고리 삭제 성공")
  void deleteCategory() {
    Member member = MemberDummy.dummy();
    Blog blog = BlogDummy.dummy(member);
    Category category = CategoryDummy.dummy(blog);
    Posts posts = PostsDummy.dummy(category);

    ReflectionTestUtils.setField(category, "posts", List.of(posts));

    when(categoryRepository.findById(category.getCategoryNo()))
            .thenReturn(Optional.of(category));

    categoryService.deleteCategory(category.getCategoryNo(), 1);

    assertTrue(category.isDeleted());
  }

  @WithMockUser("1")
  @Test
  @DisplayName("카테고리 삭제 실패 - 없는 카테고리")
  void deleteCategoryFailedNoCategory() {
    Member member = MemberDummy.dummy();
    Blog blog = BlogDummy.dummy(member);
    Category category = CategoryDummy.dummy(blog);

    when(categoryRepository.findById(category.getCategoryNo()))
            .thenReturn(Optional.empty());

    assertThrows(NotFoundException.class,
            () -> categoryService.deleteCategory(1L, 1));
  }

  @WithMockUser("1")
  @Test
  @DisplayName("카테고리 수정 실패 - 회원 미일치")
  void deleteCategoryFailedNoMember() {
    Member member = MemberDummy.dummy();
    Blog blog = BlogDummy.dummy(member);
    Category category = CategoryDummy.dummy(blog);

    when(categoryRepository.findById(category.getCategoryNo()))
            .thenReturn(Optional.of(category));

    assertThrows(AuthorizationException.class,
            () -> categoryService.deleteCategory(1L, 5));
  }
}