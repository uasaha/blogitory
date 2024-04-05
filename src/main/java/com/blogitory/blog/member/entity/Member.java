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
  private Long memberNo;

  @Column(name = "member_email")
  private String email;

  @Column(name = "member_password")
  private String password;

  @Column(name = "member_name")
  private String name;

  @Column(name = "member_profile_thumb")
  private String profileThumb;

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
}
