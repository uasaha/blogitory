package com.blogitory.blog.image.entity;

import com.blogitory.blog.blog.entity.Blog;
import com.blogitory.blog.commons.base.BaseCreatedAtEntity;
import com.blogitory.blog.imagecategory.entity.ImageCategory;
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
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Image entity.
 *
 * @author woonseok
 * @since 1.0
 **/
@Entity
@Table(name = "image")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Image extends BaseCreatedAtEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "image_no")
  private Long imageNo;

  @ManyToOne
  @JoinColumn(name = "image_category_no")
  private ImageCategory imageCategory;

  @ManyToOne
  @JoinColumn(name = "member_no")
  private Member member;

  @ManyToOne
  @JoinColumn(name = "posts_no")
  private Posts posts;

  @ManyToOne
  @JoinColumn(name = "blog_no")
  private Blog blog;

  @Column(name = "image_url")
  private String url;

  @Column(name = "image_origin_name")
  private String originName;

  @Column(name = "image_save_name")
  private String saveName;

  @Column(name = "image_extension")
  private String extension;

  @Column(name = "image_save_path")
  private String savePath;
}
