package com.blogitory.blog.blog.service.impl;

import com.blogitory.blog.blog.dto.BlogListInSettingsResponseDto;
import com.blogitory.blog.blog.repository.BlogRepository;
import com.blogitory.blog.blog.service.BlogService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * Implementation of Blog Service.
 *
 * @author woonseok
 * @since 1.0
 **/
@Service
@RequiredArgsConstructor
public class BlogServiceImpl implements BlogService {
  private final BlogRepository blogRepository;

  @Override
  public List<BlogListInSettingsResponseDto> getBlogListByMemberNo(Integer memberNo) {
    List<BlogListInSettingsResponseDto> blogs = blogRepository.getBlogListByMemberNo(memberNo);
    blogs.forEach(BlogListInSettingsResponseDto::setThumbIsNull);

    return blogs;
  }
}
