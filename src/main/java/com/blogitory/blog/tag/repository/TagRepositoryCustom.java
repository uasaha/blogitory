package com.blogitory.blog.tag.repository;

import com.blogitory.blog.tag.dto.GetTagResponseDto;
import java.util.List;
import org.springframework.data.repository.NoRepositoryBean;

/**
 * Tag repository custom for using querydsl.
 *
 * @author woonseok
 * @since 1.0
 **/
@NoRepositoryBean
public interface TagRepositoryCustom {

  /**
   * Get tag list by post.
   *
   * @param postUrl post url
   * @return tag
   */
  List<GetTagResponseDto> getTagListByPost(String postUrl);

  /**
   * Get tags by blog.
   *
   * @param blogUrl blog url
   * @return tags
   */
  List<GetTagResponseDto> getTagsByBlog(String blogUrl);
}
