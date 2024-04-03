package com.blogitory.blog.comment.entity;

import com.blogitory.blog.commons.base.BaseCreatedAtEntity;
import com.blogitory.blog.member.entity.Member;
import com.blogitory.blog.posts.entity.Posts;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 설명 작성 필!
 *
 * @author woonseok
 * @since 1.0
 **/
@Entity
@Table(name = "comment")
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Comment extends BaseCreatedAtEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "comment_no")
  private Long commentNo;

  @ManyToOne
  @JoinColumn(name = "member_no")
  private Member member;

  @ManyToOne
  @JoinColumn(name = "posts_no")
  private Posts posts;

  @ManyToOne
  @JoinColumn(name = "comment_no")
  private Comment parentComment;

  @Column(name = "comment_contents")
  private String contents;

  @Column(name = "updated_at")
  private LocalDateTime updatedAt;

  @Column(name = "comment_deleted")
  private boolean deleted;
}
