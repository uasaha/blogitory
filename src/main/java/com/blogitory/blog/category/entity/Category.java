package com.blogitory.blog.category.entity;

import com.blogitory.blog.blog.entity.Blog;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
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
 * Category entity.
 *
 * @author woonseok
 * @since 1.0
 **/
@Entity
@Table(name = "category")
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Category {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "category_no")
  private Long categoryNo;

  @JoinColumn(name = "blog_no")
  @ManyToOne(fetch = FetchType.LAZY)
  private Blog blog;

  @Column(name = "category_name")
  private String name;

  @Column(name = "category_deleted")
  private boolean deleted;
}
