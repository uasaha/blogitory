package com.blogitory.blog.heart.entity;

import com.blogitory.blog.member.entity.Member;
import com.blogitory.blog.posts.entity.Posts;

/**
 * Heart dummy.
 *
 * @author woonseok
 * @since 1.0
 **/
public class HeartDummy {
  public static Heart dummy(Member member, Posts posts) {
    Heart.Pk pk = new Heart.Pk(member.getMemberNo(), posts.getPostsNo());

    return new Heart(pk, member, posts, false);
  }
}