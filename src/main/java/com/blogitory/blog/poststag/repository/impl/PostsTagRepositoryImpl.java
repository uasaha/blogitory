package com.blogitory.blog.poststag.repository.impl;

import com.blogitory.blog.posts.entity.QPosts;
import com.blogitory.blog.poststag.entity.PostsTag;
import com.blogitory.blog.poststag.entity.QPostsTag;
import com.blogitory.blog.poststag.repository.PostsTagRepositoryCustom;
import java.util.List;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

/**
 * Implementation of PostsTagRepository for using qureydsl.
 *
 * @author woonseok
 * @since 1.0
 **/
public class PostsTagRepositoryImpl extends QuerydslRepositorySupport implements PostsTagRepositoryCustom {

  public PostsTagRepositoryImpl() {
    super(PostsTag.class);
  }

  @Override
  public List<PostsTag> findByPostsNo(Long postsNo) {
    QPosts posts = QPosts.posts;
    QPostsTag postsTag = QPostsTag.postsTag;

    return from(postsTag)
            .select(postsTag)
            .innerJoin(posts).on(postsTag.posts.postsNo.eq(posts.postsNo))
            .where(posts.postsNo.eq(postsNo))
            .fetch();
  }
}
