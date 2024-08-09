package com.blogitory.blog.posts.repository;

import com.blogitory.blog.posts.dto.response.GetPostResponseDto;
import java.util.Optional;
import org.springframework.data.repository.NoRepositoryBean;

/**
 * Posts repository custom for using querydsl.
 *
 * @author woonseok
 * @Date 2024-08-03
 * @since 1.0
 **/
@NoRepositoryBean
public interface PostsRepositoryCustom {

  /**
   * Get posts by post url.
   *
   * @param url url
   * @return post
   */
  Optional<GetPostResponseDto> getPostByPostUrl(String url);
}
