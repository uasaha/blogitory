package com.blogitory.blog.visitant.repository;

import com.blogitory.blog.visitant.entity.Visitant;
import java.time.LocalDate;
import java.util.Optional;
import org.springframework.data.repository.NoRepositoryBean;

/**
 * Visitant repository custom.
 *
 * @author woonseok
 * @Date 2024-09-10
 * @since 1.0
 **/
@NoRepositoryBean
public interface VisitantRepositoryCustom {
  /**
   * Get total visitant count of blog.
   *
   * @param blogUrl blog url
   * @return count
   */
  Integer getCountByBlogUrl(String blogUrl);

  /**
   * Get target date visitant count of blog.
   *
   * @param blogUrl blog url
   * @param date    target date
   * @return count
   */
  Integer getCountByBlogUrlAndDate(String blogUrl, LocalDate date);

  /**
   * Get visitant entity by blog and target date.
   *
   * @param blogUrl blog url
   * @param date    date
   * @return visitant
   */
  Optional<Visitant> findByBlogUrlAndDate(String blogUrl, LocalDate date);
}