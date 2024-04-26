package com.blogitory.blog.follow.repository;

import com.blogitory.blog.follow.entity.Follow;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Follow Repository.
 *
 * @author woonseok
 * @since 1.0
 **/
public interface FollowRepository extends JpaRepository<Follow, Long> {
}
