package com.blogitory.blog.blog.repository;

import com.blogitory.blog.blog.entity.Blog;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Blog repository.
 *
 * @author woonseok
 * @since 1.0
 **/
public interface BlogRepository extends JpaRepository<Blog, Long>, BlogRepositoryCustom {
  Optional<Blog> findBlogByUrlName(String urlName);
}
