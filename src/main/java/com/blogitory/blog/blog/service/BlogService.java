package com.blogitory.blog.blog.service;

import com.blogitory.blog.blog.dto.request.BlogCreateRequestDto;
import com.blogitory.blog.blog.dto.request.BlogModifyRequestDto;
import com.blogitory.blog.blog.dto.response.BlogListInSettingsResponseDto;
import java.util.List;

/**
 * Service layer for Blog.
 *
 * @author woonseok
 * @since 1.0
 **/
public interface BlogService {
  /**
   * Get Blog List by memberNo.
   *
   * @param memberNo memberNo
   * @return Blog List
   */
  List<BlogListInSettingsResponseDto> getBlogListByMemberNo(Integer memberNo);

  /**
   * Create new blog.
   *
   * @param requestDto Info for make blog
   * @param memberNo   member no
   */
  void createBlog(BlogCreateRequestDto requestDto, Integer memberNo);

  /**
   * Modify blog.
   *
   * @param urlName    urlName
   * @param requestDto Info for modify blog
   * @param memberNo   member no
   */
  void updateBlog(String urlName, BlogModifyRequestDto requestDto, Integer memberNo);

  /**
   * Quit blog.
   *
   * @param memberNo member no
   * @param url      blog url
   * @param pwd      member password
   */
  void quitBlog(Integer memberNo, String url, String pwd);
}
