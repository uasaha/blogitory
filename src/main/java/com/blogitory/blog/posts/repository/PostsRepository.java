package com.blogitory.blog.posts.repository;

import com.blogitory.blog.posts.entity.Posts;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Posts Repository.
 *
 * @author woonseok
 * @since 1.0
 **/
public interface PostsRepository extends JpaRepository<Posts, Long> {

}
