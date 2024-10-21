package com.blogitory.blog.sse.controller;

import com.blogitory.blog.security.util.SecurityUtils;
import com.blogitory.blog.sse.service.SseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

/**
 * Sse rest controller.
 *
 * @author woonseok
 * @since 1.0
 **/
@Slf4j
@RequiredArgsConstructor
@RestController
public class SseRestController {
  private final SseService sseService;

  private static final String LAST_EVENT_ID = "Last-Event-ID";
  private static final String ACCEL_BUFFERING = "X-Accel-Buffering";

  /**
   * Create new sse emitter.
   *
   * @param lastId last received notice no
   * @return sse emitter
   */
  @GetMapping("/api/notifications/subscribe")
  public ResponseEntity<SseEmitter> subscribe(
          @RequestHeader(name = LAST_EVENT_ID, required = false, defaultValue = "") String lastId) {

    if (!SecurityUtils.isAuthenticated()) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    SseEmitter emitter = sseService.connect(SecurityUtils.getCurrentUserNo());

    if (!lastId.isBlank()) {
      sseService.checkAndSend(SecurityUtils.getCurrentUserNo(), Long.parseLong(lastId));
    }

    HttpHeaders headers = new HttpHeaders();
    headers.add(ACCEL_BUFFERING, "no");

    return ResponseEntity.status(HttpStatus.OK).headers(headers).body(emitter);
  }
}
