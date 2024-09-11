package com.blogitory.blog.visitant.entity;

import com.blogitory.blog.blog.entity.Blog;
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
 * Visitant entity.
 *
 * @author woonseok
 * @Date 2024-07-31
 * @since 1.0
 **/
@Entity
@Table(name = "visitant")
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Visitant {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long visitantNo;

  @ManyToOne
  @JoinColumn(name = "blog_No")
  private Blog blog;

  @Column(name = "visitant_cnt")
  private Integer visitantCnt;

  @Column(name = "visitant_date")
  private LocalDate visitDate;

  /**
   * Constructor.
   */
  public Visitant(Blog blog, Integer visitantCnt) {
    this.blog = blog;
    this.visitantCnt = visitantCnt;
    this.visitDate = LocalDate.now();
  }

  /**
   * Add count.
   *
   * @param count count
   */
  public void addCount(Integer count) {
    this.visitantCnt += count;
  }
}
