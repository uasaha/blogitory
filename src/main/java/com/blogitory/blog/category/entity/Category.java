package com.blogitory.blog.category.entity;

import com.blogitory.blog.blog.entity.Blog;
import com.blogitory.blog.posts.entity.Posts;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;
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

  @OneToMany(mappedBy = "category")
  private List<Posts> posts = new ArrayList<>();

  /**
   * Constructor.
   *
   * @param categoryNo category no
   * @param blog       blog
   * @param name       name
   * @param deleted    is deleted
   */
  public Category(Long categoryNo, Blog blog, String name, boolean deleted) {
    this.categoryNo = categoryNo;
    this.blog = blog;
    this.name = name;
    this.deleted = deleted;
  }

  /**
   * Update category name.
   *
   * @param name new name
   */
  public void updateName(String name) {
    this.name = name;
  }

  /**
   * Delete category.
   *
   * @param id delete id
   */
  public void delete(String id) {
    this.name = id;
    this.deleted = true;
    this.posts.forEach(Posts::delete);
  }

  /**
   * Constructor Default category.
   *
   * @param blog blog
   */
  public Category(Blog blog) {
    this.blog = blog;
    this.name = "일반";
    this.deleted = false;
  }
}
