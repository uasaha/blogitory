package com.blogitory.blog.tempposts.entity;

import com.blogitory.blog.commons.base.BaseCreatedAtEntity;
import com.blogitory.blog.member.entity.Member;
import com.blogitory.blog.posts.dto.request.SaveTempPostsDto;
import jakarta.persistence.Column;
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

  @Column
  private Long blogNo;

  @Column
  private Long categoryNo;

  @Column
  private String title;

  @Column
  private String thumbnailUrl;

  @Column
  private String summary;

  @Column
  private String details;

  @Column
  private String tags;

  /**
   * Update temp posts.
   *
   * @param saveTempPostsDto tp dto for save
   */
  public void updateTempPosts(SaveTempPostsDto saveTempPostsDto) {
    this.blogNo = saveTempPostsDto.getBlogNo();
    this.title = saveTempPostsDto.getTitle();
    this.categoryNo = saveTempPostsDto.getCategoryNo();
    this.summary = saveTempPostsDto.getSummary();
    this.thumbnailUrl = saveTempPostsDto.getThumbnailUrl();
    this.details = saveTempPostsDto.getDetails();
    this.createdAt = LocalDateTime.now();

    StringBuilder sb = new StringBuilder();
    saveTempPostsDto.getTags().forEach(t -> sb.append(t).append(","));
    this.tags = sb.toString();
  }

  /**
   * Constructor.
   *
   * @param id     id
   * @param member writer
   */
  public TempPosts(UUID id, Member member) {
    this.tempPostsId = id;
    this.member = member;
    this.createdAt = LocalDateTime.now();
  }
}
