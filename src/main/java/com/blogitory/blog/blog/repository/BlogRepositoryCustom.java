package com.blogitory.blog.blog.repository;

import com.blogitory.blog.blog.dto.response.BlogListInSettingsResponseDto;
import java.util.List;
import org.springframework.data.repository.NoRepositoryBean;

/**
 * Custom repository for using querydsl.
 *
 * @author woonseok
 * @since 1.0
 **/
@NoRepositoryBean
public interface BlogRepositoryCustom {

  /**
   * Get Blogs for settings page by member no.
   *
   * @param memberNo memberNo
   * @return Blogs
   */
  List<BlogListInSettingsResponseDto> getBlogListByMemberNo(Integer memberNo);
}
