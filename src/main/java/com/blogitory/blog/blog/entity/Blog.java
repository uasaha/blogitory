package com.blogitory.blog.blog.entity;

import com.blogitory.blog.commons.base.BaseCreatedAtEntity;
import com.blogitory.blog.member.entity.Member;
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

  @Column(name = "blog_bio")
  private String bio;

  @Column(name = "blog_url_name")
  private String urlName;

  @Column(name = "blog_background")
  private String background;

  @Column(name = "blog_intro")
  private String intro;

  @Column(name = "blog_theme")
  private String theme;

  @Column(name = "blog_deleted")
  private boolean deleted;

  @Column(name = "blog_url_modified_at")
  private LocalDateTime modifiedAt;
}
