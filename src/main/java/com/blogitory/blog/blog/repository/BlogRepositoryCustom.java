package com.blogitory.blog.blog.repository;

import com.blogitory.blog.blog.dto.response.GetBlogInHeaderResponseDto;
import com.blogitory.blog.blog.dto.response.GetBlogInSettingsResponseDto;
import com.blogitory.blog.blog.dto.response.GetBlogResponseDto;
import com.blogitory.blog.blog.dto.response.GetBlogWithCategoryResponseDto;
import java.util.List;
import java.util.Optional;
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
  List<GetBlogInSettingsResponseDto> getBlogListByMemberNo(Integer memberNo);

  /**
   * Get blogs for using header.
   *
   * @param username username
   * @return blogs
   */
  List<GetBlogInHeaderResponseDto> getBlogListInHeaderByUsername(String username);

  Optional<GetBlogResponseDto> getBlogByUrl(String url);

  List<GetBlogWithCategoryResponseDto> getBlogWithCategoryList(Integer memberNo);
}
