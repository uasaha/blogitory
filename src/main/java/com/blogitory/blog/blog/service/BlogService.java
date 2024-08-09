package com.blogitory.blog.blog.service;

import com.blogitory.blog.blog.dto.request.CreateBlogRequestDto;
import com.blogitory.blog.blog.dto.request.UpdateBlogRequestDto;
import com.blogitory.blog.blog.dto.response.GetBlogInHeaderResponseDto;
import com.blogitory.blog.blog.dto.response.GetBlogInSettingsResponseDto;
import com.blogitory.blog.blog.dto.response.GetBlogResponseDto;
import com.blogitory.blog.blog.dto.response.GetBlogWithCategoryResponseDto;
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
  List<GetBlogInSettingsResponseDto> getBlogListByMemberNo(Integer memberNo);

  /**
   * Create new blog.
   *
   * @param requestDto Info for make blog
   * @param memberNo   member no
   */
  void createBlog(CreateBlogRequestDto requestDto, Integer memberNo);

  /**
   * Modify blog.
   *
   * @param urlName    urlName
   * @param requestDto Info for modify blog
   * @param memberNo   member no
   */
  void updateBlog(String urlName, UpdateBlogRequestDto requestDto, Integer memberNo);

  /**
   * Quit blog.
   *
   * @param memberNo member no
   * @param url      blog url
   * @param pwd      member password
   */
  void quitBlog(Integer memberNo, String url, String pwd);

  /**
   * Get Blog List.
   *
   * @param username username
   * @return Blog List
   */
  List<GetBlogInHeaderResponseDto> blogListForHeader(String username);

  /**
   * Get Blog by url.
   *
   * @param url url
   * @return Blog Dto
   */
  GetBlogResponseDto getBlogByUrl(String url);

  /**
   * Get Blog List with category.
   *
   * @param memberNo memberNo
   * @return blog list
   */
  List<GetBlogWithCategoryResponseDto> getBlogListWithCategory(Integer memberNo);
}
