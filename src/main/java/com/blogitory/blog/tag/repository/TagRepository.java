package com.blogitory.blog.tag.repository;

import com.blogitory.blog.tag.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Tag repository with JPA.
 *
 * @author woonseok
 * @since 1.0
 **/
public interface TagRepository extends JpaRepository<Tag, Long> {
}
