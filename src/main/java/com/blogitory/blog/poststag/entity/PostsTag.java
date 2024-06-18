package com.blogitory.blog.poststag.entity;

import com.blogitory.blog.blog.entity.Blog;
import com.blogitory.blog.posts.entity.Posts;
import com.blogitory.blog.tag.entity.Tag;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Posts Tag entity.
 *
 * @author woonseok
 * @since 1.0
 **/
@Entity
@Table(name = "posts_tag")
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostsTag {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "posts_tag_no")
  private Long postsTagNo;

  @ManyToOne
  @JoinColumn(name = "tag_no")
  private Tag tag;

  @ManyToOne
  @JoinColumn(name = "posts_no")
  private Posts posts;

  @ManyToOne
  @JoinColumn(name = "blog_no")
  private Blog blog;
}
