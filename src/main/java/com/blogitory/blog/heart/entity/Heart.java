package com.blogitory.blog.heart.entity;

import com.blogitory.blog.commons.base.BaseCreatedAtEntity;
import com.blogitory.blog.member.entity.Member;
import com.blogitory.blog.posts.entity.Posts;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import java.io.Serializable;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Heart entity.
 *
 * @author woonseok
 * @since 1.0
 **/
@Entity
@Table(name = "heart")
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Heart extends BaseCreatedAtEntity {
  @EmbeddedId
  private Pk pk;

  @ManyToOne(fetch = FetchType.LAZY)
  @MapsId("member_no")
  @JoinColumn(name = "member_no")
  private Member member;

  @ManyToOne(fetch = FetchType.LAZY)
  @MapsId("posts_no")
  @JoinColumn(name = "posts_no")
  private Posts posts;

  @Column(name = "heart_deleted")
  private boolean deleted;

  /**
   * PK of Heart
   */
  @Getter
  @EqualsAndHashCode
  @NoArgsConstructor(access = AccessLevel.PROTECTED)
  @AllArgsConstructor
  @Embeddable
  public static class Pk implements Serializable {
    private Long memberNo;
    private Long postsNo;
  }
}
