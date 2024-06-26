package com.blogitory.blog.follow.entity;

import com.blogitory.blog.member.entity.Member;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Follow entity.
 *
 * @author woonseok
 * @since 1.0
 **/
@Entity
@Table(name = "follow")
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Follow {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "follow_no")
  private Long followNo;

  @ManyToOne
  @JoinColumn(name = "follow_to_no")
  private Member followTo;

  @ManyToOne
  @JoinColumn(name = "follow_from_no")
  private Member followFrom;
}
