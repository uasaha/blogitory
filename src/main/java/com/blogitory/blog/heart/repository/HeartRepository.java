package com.blogitory.blog.heart.repository;

import com.blogitory.blog.heart.entity.Heart;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Heart Repository.
 *
 * @author woonseok
 * @since 1.0
 **/
public interface HeartRepository extends JpaRepository<Heart, Long>, HeartRepositoryCustom {
}
