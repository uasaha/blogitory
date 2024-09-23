package com.blogitory.blog.viewer.entity;

import com.blogitory.blog.posts.entity.Posts;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDate;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Viewer entity.
 *
 * @author woonseok
 * @since 1.0
 **/
@Entity
@Table(name = "viewer")
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Viewer {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long viewerNo;

  @ManyToOne
  @JoinColumn(name = "posts_no")
  private Posts posts;

  @Column(name = "viewer_cnt")
  private Integer viewerCnt;

  @Column(name = "viewer_date")
  private LocalDate viewerDate;

  /**
   * Constructor.
   */
  public Viewer(Posts posts, Integer viewerCnt) {
    this.posts = posts;
    this.viewerCnt = viewerCnt;
    this.viewerDate = LocalDate.now();
  }

  /**
   * Add count.
   *
   * @param cnt count
   */
  public void addCount(Integer cnt) {
    this.viewerCnt += cnt;
  }
}
