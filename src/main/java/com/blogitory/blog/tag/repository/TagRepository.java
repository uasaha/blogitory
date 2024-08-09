package com.blogitory.blog.tag.repository;

import com.blogitory.blog.tag.entity.Tag;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Tag repository with JPA.
 *
 * @author woonseok
 * @since 1.0
 **/
public interface TagRepository extends JpaRepository<Tag, Long>, TagRepositoryCustom {

  /**
   * is tag exist.
   */
  boolean existsByName(String name);

  /**
   * Find tag by name.
   *
   * @param name tag name
   * @return tag
   */
  Optional<Tag> findByName(String name);
}
