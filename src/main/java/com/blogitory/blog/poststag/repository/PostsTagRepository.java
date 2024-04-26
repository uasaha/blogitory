package com.blogitory.blog.poststag.repository;

import com.blogitory.blog.poststag.entity.PostsTag;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * PostsTag Repository.
 *
 * @author woonseok
 * @since 1.0
 **/
public interface PostsTagRepository extends JpaRepository<PostsTag, Long> {
}
