package com.blogitory.blog.tempposts.entity;

import com.blogitory.blog.commons.base.BaseCreatedAtEntity;
import com.blogitory.blog.member.entity.Member;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Temp posts entity.
 *
 * @author woonseok
 * @since 1.0
 **/
@Entity
@Table(name = "temp_posts")
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TempPosts extends BaseCreatedAtEntity {

  @Id
  private UUID tempPostsId;

  @ManyToOne
  @JoinColumn(name = "member_no")
  private Member member;

  /**
   * Update create at.
   */
  public void updateCreateAt() {
    this.createdAt = LocalDateTime.now();
  }
}
