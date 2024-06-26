package com.blogitory.blog.link.entity;

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
 * Link entity.
 *
 * @author woonseok
 * @since 1.0
 **/
@Entity
@Table(name = "link")
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Link {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "link_no")
  private Long linkNo;

  @ManyToOne
  @JoinColumn(name = "member_no")
  private Member member;

  @Column(name = "link_url")
  private String url;
}
