package com.blogitory.blog.notice.entity;

import com.blogitory.blog.commons.base.BaseCreatedAtEntity;
import com.blogitory.blog.member.entity.Member;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Notice entity.
 *
 * @author woonseok
 * @since 1.0
 **/
@Entity
@Table(name = "notice")
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Notice extends BaseCreatedAtEntity {
  @Id
  @Column(name = "notice_no")
  private Long noticeNo;

  @ManyToOne
  @JoinColumn(name = "member_no")
  private Member member;

  @Column(name = "notice_relation")
  private Long relation;

  @Column(name = "notice_type")
  private String type;

  @Column(name = "notice_msg")
  private String msg;
}
