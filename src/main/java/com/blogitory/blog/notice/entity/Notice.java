package com.blogitory.blog.notice.entity;

import com.blogitory.blog.commons.base.BaseCreatedAtEntity;
import com.blogitory.blog.member.entity.Member;
import com.blogitory.blog.notice.enums.NoticeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
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
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "notice_no")
  private Long noticeNo;

  @ManyToOne
  @JoinColumn(name = "member_no")
  private Member member;

  @Column(name = "notice_sender")
  private String sender;

  @Column(name = "notice_url")
  private String url;

  @Enumerated(EnumType.STRING)
  @Column(name = "notice_type")
  private NoticeType type;

  @Column(name = "notice_content")
  private String content;

  @Column(name = "notice_read")
  private boolean read;

  /**
   * Constructor.
   *
   * @param member  member
   * @param sender  sender name
   * @param url     url
   * @param type    type
   * @param content content
   */
  @Builder
  public Notice(Member member, String sender, String url, NoticeType type, String content) {
    this.member = member;
    this.sender = sender;
    this.url = url;
    this.type = type;
    this.content = content;
    this.read = false;
  }

  /**
   * Read notice.
   */
  public void read() {
    this.read = true;
  }
}
