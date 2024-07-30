package com.blogitory.blog.blog.service.impl;

import com.blogitory.blog.blog.dto.request.BlogCreateRequestDto;
import com.blogitory.blog.blog.dto.request.BlogModifyRequestDto;
import com.blogitory.blog.blog.dto.response.BlogListInSettingsResponseDto;
import com.blogitory.blog.blog.entity.Blog;
import com.blogitory.blog.blog.exception.BlogLimitException;
import com.blogitory.blog.blog.repository.BlogRepository;
import com.blogitory.blog.blog.service.BlogService;
import com.blogitory.blog.commons.exception.NotFoundException;
import com.blogitory.blog.member.entity.Member;
import com.blogitory.blog.member.repository.MemberRepository;
import com.blogitory.blog.security.exception.AuthorizationException;
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
  private final PasswordEncoder passwordEncoder;

  private static final Integer MAX_BLOG_SIZE = 3;
  public static final String BLOG_DELETED = "B-D-";

  /**
   * {@inheritDoc}
   */
  @Override
  @Transactional(readOnly = true)
  public List<BlogListInSettingsResponseDto> getBlogListByMemberNo(Integer memberNo) {
    List<BlogListInSettingsResponseDto> blogs = blogRepository.getBlogListByMemberNo(memberNo);
    blogs.forEach(BlogListInSettingsResponseDto::setThumbIsNull);

    return blogs;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void createBlog(BlogCreateRequestDto requestDto, Integer memberNo) {
    Member member = memberRepository.findById(memberNo)
            .orElseThrow(() -> new NotFoundException(Member.class));

    if (member.getBlogs().size() >= MAX_BLOG_SIZE) {
      throw new BlogLimitException();
    }

    Blog blog = requestDto.of(member);

    blogRepository.save(blog);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void updateBlog(String urlName, BlogModifyRequestDto requestDto, Integer memberNo) {
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
}
