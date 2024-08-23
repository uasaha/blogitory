package com.blogitory.blog.member.entity;

import com.blogitory.blog.blog.entity.Blog;
import com.blogitory.blog.commons.base.BaseCreatedAtEntity;
import com.blogitory.blog.link.entity.Link;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;
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
  @Column(name = "member_no")
  private Integer memberNo;

  @Column(name = "member_email")
  private String email;

  @Column(name = "member_password")
  private String password;

  @Column(name = "member_username")
  private String username;

  @Column(name = "member_name")
  private String name;

  @Column(name = "member_bio")
  private String bio;

  @Column(name = "member_profile_thumb")
  private String profileThumb;

  @Column(name = "member_intro_email")
  private String introEmail;

  @OneToMany(mappedBy = "member")
  private List<Link> links = new ArrayList<>();

  @OneToMany(mappedBy = "member")
  private List<Blog> blogs = new ArrayList<>();

  @Column(name = "member_blocked")
  private boolean blocked;

  @Column(name = "member_left")
  private boolean left;

  @Column(name = "member_oauth")
  private String oauth;

  @Column(name = "member_follow_alert")
  private boolean followAlert;

  @Column(name = "member_comment_alert")
  private boolean commentAlert;

  @Column(name = "member_heart_alert")
  private boolean heartAlert;

  @Column(name = "member_new_alert")
  private boolean newAlert;

  /**
   * Constructor no links, no roles, no blog.
   *
   * @param memberNo     memberNo
   * @param email        email
   * @param password     password
   * @param username     username
   * @param name         name
   * @param bio          bio
   * @param profileThumb profileThumb
   * @param introEmail   introEmail
   * @param blocked      isBlocked
   * @param left         isLeft
   * @param oauth        isOauth
   */
  @Builder
  public Member(Integer memberNo,
                String email,
                String password,
                String username,
                String name,
                String bio,
                String profileThumb,
                String introEmail,
                boolean blocked,
                boolean left,
                String oauth,
                boolean followAlert,
                boolean commentAlert,
                boolean heartAlert,
                boolean newAlert) {
    this.memberNo = memberNo;
    this.email = email;
    this.password = password;
    this.username = username;
    this.name = name;
    this.bio = bio;
    this.profileThumb = profileThumb;
    this.introEmail = introEmail;
    this.blocked = blocked;
    this.left = left;
    this.oauth = oauth;
    this.followAlert = followAlert;
    this.commentAlert = commentAlert;
    this.heartAlert = heartAlert;
    this.newAlert = newAlert;
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

  public void updateBio(String bio) {
    this.bio = bio;
  }

  public void updateFollowAlert(boolean followAlert) {
    this.followAlert = followAlert;
  }

  public void updateCommentAlert(boolean commentAlert) {
    this.commentAlert = commentAlert;
  }

  public void updateHeartAlert(boolean heartAlert) {
    this.heartAlert = heartAlert;
  }

  public void updateNewAlert(boolean newAlert) {
    this.newAlert = newAlert;
  }

  public void updatePassword(String password) {
    this.password = password;
  }

  /**
   * Quit member.
   *
   * @param deleteMsg delete code
   */
  public void deleteMember(String deleteMsg) {
    this.left = true;
    this.username = deleteMsg;
    this.email = deleteMsg;
  }
}
