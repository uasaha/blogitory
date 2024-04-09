package com.blogitory.blog.blog.entity;

import com.blogitory.blog.commons.base.BaseCreatedAtEntity;
import com.blogitory.blog.member.entity.Member;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
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
  @Column(name = "blog_no")
  private Integer memberNo;

  @MapsId("member_no")
  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "blog_no")
  private Member member;

  @Column(name = "blog_name")
  private String name;

  @Column(name = "blog_url_name")
  private String urlName;

  @Column(name = "blog_background")
  private String background;

  @Column(name = "blog_intro")
  private String intro;

  @Column(name = "blog_theme")
  private boolean theme;
}
