package com.blogitory.blog.notice.dto;

import com.blogitory.blog.notice.enums.NoticeType;

/**
 * Notice response.
 *
 * @author woonseok
 * @since 1.0
 **/
public interface NoticeResponse {

  /**
   * Get notice type.
   *
   * @return type
   */
  NoticeType getType();

  /**
   * Get sender name.
   *
   * @return sender name
   */
  String getSenderName();

  /**
   * Get url.
   *
   * @return url
   */
  String getUrl();

  /**
   * Get message.
   *
   * @return message
   */
  String getMessage();

  /**
   * Get content.
   *
   * @return content
   */
  default String getContent() {
    return "";
  }
}
