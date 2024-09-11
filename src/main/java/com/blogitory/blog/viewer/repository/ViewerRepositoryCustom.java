package com.blogitory.blog.viewer.repository;

import com.blogitory.blog.viewer.entity.Viewer;
import java.time.LocalDate;
import java.util.Optional;
import org.springframework.data.repository.NoRepositoryBean;

/**
 * Viewer repository custom.
 *
 * @author woonseok
 * @Date 2024-09-09
 * @since 1.0
 **/
@NoRepositoryBean
public interface ViewerRepositoryCustom {

  /**
   * Get total counts by posts url.
   *
   * @param postsUrl post url
   * @return count
   */
  Integer getCountByPostsUrl(String postsUrl);

  /**
   * Get viewer entity by posts url and target date.
   *
   * @param postsUrl post url
   * @param date     date
   * @return viewer entity
   */
  Optional<Viewer> findByPostsUrlAndDate(String postsUrl, LocalDate date);
}