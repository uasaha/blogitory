package com.blogitory.blog.member.entity;

import com.blogitory.blog.commons.base.BaseCreatedAtEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Member entity.
 *
 * @author woonseok
 * @since 1.0
 **/
@Entity
@Table(name = "member")
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseCreatedAtEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column
  private Integer memberNo;

  @Column(name = "member_email")
  private String email;

  @Column(name = "member_password")
  private String password;

  @Column(name = "member_name")
  private String name;

  @Column(name = "member_profile_thumb")
  private String profileThumb;

  @Column(name = "member_intro_email")
  private String introEmail;

  @Column(name = "member_github")
  private String github;

  @Column(name = "member_twitter")
  private String twitter;

  @Column(name = "member_facebook")
  private String facebook;

  @Column(name = "member_homepage")
  private String homepage;

  @Column(name = "member_blocked")
  private boolean blocked;

  @Column(name = "member_left")
  private boolean left;

  /**
   * Constructor of Member for Signup.
   *
   * @param email email.
   * @param password password.
   * @param name name.
   */
  @Builder
  public Member(String email, String password, String name) {
    this.email = email;
    this.password = password;
    this.name = name;
  }

  public void updateThumbnail(String url) {
    this.profileThumb = url;
  }

  public void updateName(String name) {
    this.name = name;
  }

  public void updateOpenEmail(String email) {
    this.introEmail = email;
  }

  public void updateGithub(String github) {
    this.github = github;
  }

  public void updateFacebook(String facebook) {
    this.facebook = facebook;
  }

  public void updateX(String x) {
    this.twitter = x;
  }

  public void updateHomepage(String homepage) {
    this.homepage = homepage;
  }
}
