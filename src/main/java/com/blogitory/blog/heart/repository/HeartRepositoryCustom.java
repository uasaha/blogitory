package com.blogitory.blog.heart.repository;

import com.blogitory.blog.heart.entity.Heart;
import java.util.Optional;
import org.springframework.data.repository.NoRepositoryBean;

/**
 * Custom heart repository for using querydsl.
 *
 * @author woonseok
 * @Date 2024-08-29
 * @since 1.0
 **/
@NoRepositoryBean
public interface HeartRepositoryCustom {
  /**
   * Find by member no and posts url.
   *
   * @param memberNo member no
   * @param postsUrl post url
   * @return heart entity
   */
  Optional<Heart> findByMemberNoAndPostsUrl(Integer memberNo, String postsUrl);

  /**
   * Get count of hearts.
   *
   * @param postsUrl post url
   * @return count
   */
  Long getHeartCountsByPost(String postsUrl);
}
