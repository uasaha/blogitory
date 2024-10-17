package com.blogitory.blog.notice.dto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Get notice response for page.
 *
 * @author woonseok
 * @since 1.0
 **/
@Getter
@AllArgsConstructor
public class GetNoticeResponseDto {
  private Long noticeNo;
  private NoticeResponse response;
  private boolean read;
  private LocalDateTime createdAt;
}
