package com.blogitory.blog.follow.repository;

import org.springframework.data.repository.NoRepositoryBean;

/**
 * Custom Follow Repository for using querydsl.
 *
 * @author woonseok
 * @since 1.0
 **/
@NoRepositoryBean
public interface FollowRepositoryCustom {
  Long countFollowee(Integer followFromNo);

  Long countFollower(Integer followToNo);
}
