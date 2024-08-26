package com.blogitory.blog.category.service.impl;

import com.blogitory.blog.blog.entity.Blog;
import com.blogitory.blog.blog.repository.BlogRepository;
import com.blogitory.blog.category.dto.CreateCategoryResponseDto;
import com.blogitory.blog.category.entity.Category;
import com.blogitory.blog.category.exception.DuplicateCategoryException;
import com.blogitory.blog.category.repository.CategoryRepository;
import com.blogitory.blog.category.service.CategoryService;
import com.blogitory.blog.commons.exception.NotFoundException;
import com.blogitory.blog.member.entity.Member;
import com.blogitory.blog.security.exception.AuthorizationException;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Category service implementation.
 *
 * @author woonseok
 * @Date 2024-07-15
 * @since 1.0
 **/
@Service
@Transactional
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
  private final BlogRepository blogRepository;
  private final CategoryRepository categoryRepository;

  /**
   * {@inheritDoc}
   */
  @Override
  public CreateCategoryResponseDto createCategory(String blogUrl, String name, Integer memberNo) {
    Blog blog = blogRepository.findBlogByUrlName(blogUrl)
            .orElseThrow(() -> new NotFoundException(Blog.class));

    if (!blog.getMember().getMemberNo().equals(memberNo)) {
      throw new AuthorizationException();
    }

    List<Category> categories = blog.getCategories();

    if (categories.stream().anyMatch(c -> c.getName().equals(name))) {
      throw new DuplicateCategoryException(name);
    }

    Category category = new Category(
            null,
            blog,
            name,
            false);

    category = categoryRepository.save(category);

    return new CreateCategoryResponseDto(
            category.getCategoryNo(),
            category.getName()
    );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void updateCategory(Long categoryNo, String name, Integer memberNo) {
    Category category = categoryRepository.findById(categoryNo)
            .orElseThrow(() -> new NotFoundException(Category.class));

    Blog blog = category.getBlog();
    Member member = blog.getMember();

    if (!member.getMemberNo().equals(memberNo)) {
      throw new AuthorizationException();
    }

    List<Category> categories = blog.getCategories();

    if (categories.stream().anyMatch(c -> c.getName().equals(name))) {
      throw new DuplicateCategoryException(name);
    }

    category.updateName(name);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void deleteCategory(Long categoryNo, Integer memberNo) {
    Category category = categoryRepository.findById(categoryNo)
            .orElseThrow(() -> new NotFoundException(Category.class));

    String uuid = UUID.randomUUID().toString();

    Member member = category.getBlog().getMember();

    if (!member.getMemberNo().equals(memberNo)) {
      throw new AuthorizationException();
    }

    category.delete(uuid);
  }
}
