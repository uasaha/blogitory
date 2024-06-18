package com.blogitory.blog.comment.entity;

import com.blogitory.blog.member.entity.Member;
import com.blogitory.blog.posts.entity.Posts;
import java.time.LocalDateTime;

/**
 * 설명 작성 필!
 *
 * @author woonseok
 * @since 1.0
 */
public class CommentDummy {
  /**
   * Dummy comment.
   *
   * @param member the member
   * @param posts  the posts
   * @return the comment
   */
  public static Comment dummy(Member member, Posts posts) {
    return new Comment(
            1L,
            member,
            posts,
            null,
            "contents",
            LocalDateTime.of(2000, 12, 12, 12, 12, 12),
            false);
  }

  /**
   * Dummy comment.
   *
   * @param member the member
   * @param posts  the posts
   * @param parent the parent
   * @return the comment
   */
  public static Comment dummy(Member member, Posts posts, Comment parent) {
    return new Comment(
            1L,
            member,
            posts,
            parent,
            "contents",
            LocalDateTime.of(2000, 12, 12, 12, 12, 12),
            false
            );
  }
}
