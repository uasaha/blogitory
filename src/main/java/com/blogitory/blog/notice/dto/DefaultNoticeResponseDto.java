package com.blogitory.blog.notice.dto;

import com.blogitory.blog.notice.entity.Notice;
import com.blogitory.blog.notice.enums.NoticeType;
import java.util.Objects;
import lombok.Getter;

/**
 * Default Notice response dto.
 *
 * @author woonseok
 * @since 1.0
 **/
@Getter
public class DefaultNoticeResponseDto implements NoticeResponse {
  private NoticeType type;
  private String url;
  private String senderName;
  private String content;
  private String message;

  /**
   * Constructor.
   *
   * @param type       notice type
   * @param url        url
   * @param senderName sender name
   * @param content    content
   */
  public DefaultNoticeResponseDto(NoticeType type, String url, String senderName, String content) {
    this.type = type;
    this.url = url;
    this.senderName = senderName;
    this.content = content;

    initMessage();
  }

  /**
   * Constructor.
   *
   * @param type       type
   * @param url        url
   * @param senderName sender name
   */
  public DefaultNoticeResponseDto(NoticeType type, String url, String senderName) {
    this(type, url, senderName, null);
  }

  /**
   * Create instance through Notice entity.
   *
   * @param notice entity
   * @return new instance
   */
  public static NoticeResponse of(Notice notice) {
    return new DefaultNoticeResponseDto(notice.getType(),
            notice.getUrl(), notice.getSender(), notice.getContent());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getContent() {
    return Objects.isNull(this.content) ? "" : this.content;
  }

  /**
   * Init message.
   */
  private void initMessage() {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append(this.senderName);

    switch (this.type) {
      case FOLLOW -> stringBuilder.append("님이 회원님을 팔로우하기 시작했습니다.");
      case HEART -> stringBuilder.append("님이 회원님의 게시글에 좋아요를 남겼습니다.");
      case COMMENT -> stringBuilder.append("님이 회원님의 게시글에 새로운 댓글을 작성하였습니다.");
      case NEW_POSTS -> stringBuilder.append("님이 새로운 게시글을 작성하였습니다.");
      default -> {
        stringBuilder.append("새로운 알림이 도착했습니다.");
        this.url = "/notifications";
      }
    }

    this.message = stringBuilder.toString();
  }
}
