package com.blogitory.blog.blog.service.impl;

import com.blogitory.blog.blog.dto.request.CreateBlogRequestDto;
import com.blogitory.blog.blog.dto.request.UpdateBlogRequestDto;
import com.blogitory.blog.blog.dto.response.GetBlogInHeaderResponseDto;
import com.blogitory.blog.blog.dto.response.GetBlogInSettingsResponseDto;
import com.blogitory.blog.blog.dto.response.GetBlogResponseDto;
import com.blogitory.blog.blog.dto.response.GetBlogWithCategoryResponseDto;
import com.blogitory.blog.blog.entity.Blog;
import com.blogitory.blog.blog.exception.BlogLimitException;
import com.blogitory.blog.blog.repository.BlogRepository;
import com.blogitory.blog.blog.service.BlogService;
import com.blogitory.blog.category.entity.Category;
import com.blogitory.blog.category.repository.CategoryRepository;
import com.blogitory.blog.commons.exception.NotFoundException;
import com.blogitory.blog.member.entity.Member;
import com.blogitory.blog.member.repository.MemberRepository;
import com.blogitory.blog.security.exception.AuthorizationException;
import com.blogitory.blog.tag.repository.TagRepository;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementation of Blog Service.
 *
 * @author woonseok
 * @since 1.0
 **/
@Service
@Transactional
@RequiredArgsConstructor
public class BlogServiceImpl implements BlogService {
  private final MemberRepository memberRepository;
  private final BlogRepository blogRepository;
  private final CategoryRepository categoryRepository;
  private final TagRepository tagRepository;
  private final PasswordEncoder passwordEncoder;

  private static final Integer MAX_BLOG_SIZE = 3;
  public static final String BLOG_DELETED = "B-D-";

  /**
   * {@inheritDoc}
   */
  @Override
  @Transactional(readOnly = true)
  public List<GetBlogInSettingsResponseDto> getBlogListByMemberNo(Integer memberNo) {
    List<GetBlogInSettingsResponseDto> blogs = blogRepository.getBlogListByMemberNo(memberNo);
    blogs.forEach(GetBlogInSettingsResponseDto::setThumbIsNull);

    return blogs;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void createBlog(CreateBlogRequestDto requestDto, Integer memberNo) {
    Member member = memberRepository.findById(memberNo)
            .orElseThrow(() -> new NotFoundException(Member.class));

    List<Blog> blogs = member.getBlogs()
            .stream().filter(blog -> !blog.isDeleted()).toList();

    if (blogs.size() >= MAX_BLOG_SIZE) {
      throw new BlogLimitException();
    }

    Blog blog = requestDto.of(member);
    blog = blogRepository.save(blog);

    Category category = new Category(blog);
    categoryRepository.save(category);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void updateBlog(String urlName, UpdateBlogRequestDto requestDto, Integer memberNo) {
    Blog blog = blogRepository.findBlogByUrlName(urlName)
            .orElseThrow(() -> new NotFoundException(Blog.class));

    if (!blog.getMember().getMemberNo().equals(memberNo)) {
      throw new AuthorizationException();
    }

    blog.updateName(requestDto.getName());
    blog.updateBio(requestDto.getBio());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void quitBlog(Integer memberNo, String url, String pwd) {
    Member member = memberRepository.findById(memberNo)
            .orElseThrow(() -> new NotFoundException(Member.class));

    if (!passwordEncoder.matches(pwd, member.getPassword())) {
      throw new AuthorizationException();
    }

    Blog blog = memberRepository.findById(memberNo)
            .orElseThrow(() -> new NotFoundException(Member.class))
            .getBlogs().stream().filter(b -> b.getUrlName().equals(url)).findFirst()
            .orElseThrow(() -> new NotFoundException(Member.class));

    String msg = BLOG_DELETED + UUID.randomUUID();

    blog.quitBlog(msg);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  @Transactional(readOnly = true)
  public List<GetBlogInHeaderResponseDto> blogListForHeader(String username) {
    Member member = memberRepository.findByUsername(username)
            .orElseThrow(() -> new NotFoundException(Member.class));

    return blogRepository.getBlogListInHeaderByUsername(member.getUsername());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  @Transactional(readOnly = true)
  public GetBlogResponseDto getBlogByUrl(String url) {
    GetBlogResponseDto responseDto = blogRepository.getBlogByUrl(url)
            .orElseThrow(() -> new NotFoundException(Blog.class));

    responseDto.categories(categoryRepository.getCategoriesByBlog(url));
    responseDto.tags(tagRepository.getTagsByBlog(url));

    return responseDto;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  @Transactional(readOnly = true)
  public List<GetBlogWithCategoryResponseDto> getBlogListWithCategory(Integer memberNo) {
    List<GetBlogWithCategoryResponseDto> blogList =
            blogRepository.getBlogWithCategoryList(memberNo);

    blogList.forEach(GetBlogWithCategoryResponseDto::distinct);

    return blogList;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  @Transactional(readOnly = true)
  public GetBlogResponseDto getBlogForMy(Integer memberNo, String url) {
    GetBlogResponseDto responseDto = blogRepository.getBlogByUrl(url)
            .orElseThrow(() -> new NotFoundException(Blog.class));

    Member member = memberRepository.findById(memberNo)
            .orElseThrow(() -> new NotFoundException(Member.class));

    if (responseDto.getUsername().equals(member.getUsername())) {
      return responseDto;
    }

    throw new AuthorizationException();
  }
}
