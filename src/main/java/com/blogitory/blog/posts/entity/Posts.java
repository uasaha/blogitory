package com.blogitory.blog.posts.entity;

import com.blogitory.blog.category.entity.Category;
import com.blogitory.blog.commons.base.BaseCreatedAtEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Posts entity.
 *
 * @author woonseok
 * @since 1.0
 **/
@Entity
@Table(name = "posts")
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Posts extends BaseCreatedAtEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "posts_no")
  private Long postsNo;

  @JoinColumn(name = "category_no")
  @ManyToOne(fetch = FetchType.LAZY)
  private Category category;

  @Column(name = "posts_url")
  private String url;

  @Column(name = "posts_subject")
  private String subject;

  @Column(name = "posts_summary")
  private String summary;

  @Column(name = "posts_views")
  private Integer views;

  @Column(name = "posts_thumbnail")
  private String thumbnail;

  @Column(name = "posts_detail")
  private String detail;

  @Column(name = "updated_at")
  private LocalDateTime updatedAt;

  @Column(name = "posts_open")
  private boolean open;

  @Column(name = "posts_deleted")
  private boolean deleted;

  /**
   * Constructor for create.
   *
   * @param category  category
   * @param url       url
   * @param subject   subject
   * @param summary   summary
   * @param thumbnail thumbnail
   * @param detail    detail
   */
  @Builder
  public Posts(Category category,
               String url,
               String subject,
               String summary,
               String thumbnail,
               String detail) {
    this.category = category;
    this.url = url;
    this.subject = subject;
    this.summary = summary;
    this.views = 0;
    this.thumbnail = thumbnail;
    this.detail = detail;
    this.open = true;
  }

  /**
   * Modify posts.
   *
   * @param title title
   * @param summary summary
   * @param content content
   * @param thumb thumb
   */
  public void modify(String title, String summary, String content, String thumb) {
    this.subject = title;
    this.summary = summary;
    this.detail = content;
    this.thumbnail = thumb;
    this.updatedAt = LocalDateTime.now();
  }

  /**
   * Delete posts.
   */
  public void delete() {
    this.deleted = true;
  }

  /**
   * Close posts.
   */
  public void close() {
    this.open = false;
  }

  /**
   * Open posts.
   */
  public void open() {
    this.open = true;
  }
}
