package com.blogitory.blog.blog.service;

import com.blogitory.blog.blog.dto.BlogListInSettingsResponseDto;
import java.util.List;

/**
 * Service layer for Blog.
 *
 * @author woonseok
 * @since 1.0
 **/
public interface BlogService {
  List<BlogListInSettingsResponseDto> getBlogListByMemberNo(Integer memberNo);
}
