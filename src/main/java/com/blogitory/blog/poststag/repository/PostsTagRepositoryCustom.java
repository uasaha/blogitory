package com.blogitory.blog.poststag.repository;

import com.blogitory.blog.poststag.entity.PostsTag;
import java.util.List;
import org.springframework.data.repository.NoRepositoryBean;

/**
 * Custom PostsTagRepository for using querydsl.
 *
 * @author woonseok
 * @since 1.0
 **/
@NoRepositoryBean
public interface PostsTagRepositoryCustom {
  /**
   * Find PostsTag by posts' no.
   *
   * @param postsNo post no
   * @return PostsTag list
   */
  List<PostsTag> findByPostsNo(Long postsNo);
}
