package com.blogitory.blog.tag.repository;

import com.blogitory.blog.tag.dto.GetTagResponseDto;
import java.util.List;
import org.springframework.data.repository.NoRepositoryBean;

/**
 * Tag repository custom for using querydsl.
 *
 * @author woonseok
 * @Date 2024-08-05
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
}
