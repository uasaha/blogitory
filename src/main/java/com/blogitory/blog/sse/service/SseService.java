package com.blogitory.blog.sse.service;

import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

/**
 * Sse service.
 *
 * @author woonseok
 * @since 1.0
 **/
public interface SseService {

  /**
   * Create new SseEmitter.
   *
   * @param memberNo member no
   * @return sse emitter
   */
  SseEmitter connect(Integer memberNo);

  /**
   * Check lost notice, and send.
   *
   * @param memberNo     member no
   * @param lastNoticeNo last received event no
   */
  void checkAndSend(Integer memberNo, Long lastNoticeNo);

  /**
   * Send to sse emitter.
   *
   * @param noticeNo       notice no
   * @param targetMemberNo member no
   * @param body           body
   */
  void send(Long noticeNo, Integer targetMemberNo, Object body);
}
