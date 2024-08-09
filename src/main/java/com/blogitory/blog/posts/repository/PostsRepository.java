package com.blogitory.blog.posts.repository;

import com.blogitory.blog.posts.entity.Posts;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Posts Repository.
 *
 * @author woonseok
 * @since 1.0
 **/
public interface PostsRepository extends JpaRepository<Posts, Long>, PostsRepositoryCustom {
  boolean existsByUrl(String url);

  Optional<Posts> findByUrl(String url);
}
