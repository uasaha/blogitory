package com.blogitory.blog.blog.entity;

import com.blogitory.blog.category.entity.Category;
import com.blogitory.blog.commons.base.BaseCreatedAtEntity;
import com.blogitory.blog.member.entity.Member;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Blog entity.
 *
 * @author woonseok
 * @since 1.0
 **/
@Entity
@Table(name = "blog")
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Blog extends BaseCreatedAtEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "blog_no")
  private Long blogNo;

  @ManyToOne
  @JoinColumn(name = "member_no")
  private Member member;

  @Column(name = "blog_name")
  private String name;

  @Column(name = "blog_bio")
  private String bio;

  @Column(name = "blog_url_name")
  private String urlName;

  @Column(name = "blog_background")
  private String background;

  @Column(name = "blog_theme")
  private String theme;

  @Column(name = "blog_deleted")
  private boolean deleted;

  @Column(name = "blog_url_modified_at")
  private LocalDateTime modifiedAt;

  @OneToMany(mappedBy = "blog")
  private List<Category> categories = new ArrayList<>();

  /**
   * Constructor for Blog.
   *
   * @param blogNo     blogNo
   * @param member     member
   * @param name       name
   * @param bio        bio
   * @param urlName    url
   * @param background picture
   * @param theme      theme
   * @param deleted    isDeleted
   * @param modifiedAt modifiedAt
   */
  public Blog(Long blogNo, Member member, String name, String bio, String urlName,
              String background, String theme, boolean deleted, LocalDateTime modifiedAt) {
    this.blogNo = blogNo;
    this.member = member;
    this.name = name;
    this.bio = bio;
    this.urlName = urlName;
    this.background = background;
    this.theme = theme;
    this.deleted = deleted;
    this.modifiedAt = modifiedAt;
  }

  /**
   * Update blog name.
   *
   * @param name new name
   */
  public void updateName(String name) {
    this.name = name;
  }

  /**
   * Update blog bio.
   *
   * @param bio new bio
   */
  public void updateBio(String bio) {
    this.bio = bio;
  }

  /**
   * Update blog background picture.
   *
   * @param background new picture
   */
  public void updateBackground(String background) {
    this.background = background;
  }

  /**
   * Delete blog.
   *
   * @param deleteMsg msg
   */
  public void quitBlog(String deleteMsg) {
    this.deleted = true;
    this.name = deleteMsg;
    this.urlName = deleteMsg;
  }
}
