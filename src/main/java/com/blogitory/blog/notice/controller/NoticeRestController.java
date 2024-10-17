package com.blogitory.blog.notice.controller;

import com.blogitory.blog.commons.annotaion.RoleUser;
import com.blogitory.blog.commons.dto.Pages;
import com.blogitory.blog.notice.dto.GetNoticeResponseDto;
import com.blogitory.blog.notice.service.NoticeService;
import com.blogitory.blog.security.util.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Notice rest controller.
 *
 * @author woonseok
 * @since 1.0
 **/
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/notifications")
public class NoticeRestController {
  private final NoticeService noticeService;

  /**
   * Read notice.
   *
   * @param noticeNo notice no
   * @return 200
   */
  @RoleUser
  @GetMapping("/{noticeNo}")
  public ResponseEntity<Void> readNotice(@PathVariable Long noticeNo) {
    noticeService.read(SecurityUtils.getCurrentUserNo(), noticeNo);

    return ResponseEntity.ok().build();
  }

  /**
   * Delete notice.
   *
   * @param noticeNo notice no
   * @return 204
   */
  @RoleUser
  @DeleteMapping("/{noticeNo}")
  public ResponseEntity<Void> deleteNotice(@PathVariable Long noticeNo) {
    noticeService.delete(SecurityUtils.getCurrentUserNo(), noticeNo);

    return ResponseEntity.noContent().build();
  }

  /**
   * Get notices by pageable.
   *
   * @param pageable pageable
   * @return notices
   */
  @RoleUser
  @GetMapping
  public ResponseEntity<Pages<GetNoticeResponseDto>> getNotices(
          @PageableDefault(size = 30) Pageable pageable) {
    return ResponseEntity.ok(
            noticeService.getAllNotice(pageable, SecurityUtils.getCurrentUserNo()));
  }
}
