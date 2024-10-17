package com.blogitory.blog.sse.repository;

import java.util.List;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

/**
 * Sse repository.
 *
 * @author woonseok
 * @since 1.0
 **/
public interface SseRepository {

  /**
   * Save sse emitter.
   *
   * @param memberNo member no
   * @param emitter  new emitter
   * @return emitter
   */
  SseEmitter save(Integer memberNo, SseEmitter emitter);

  /**
   * Find all emitters by member no.
   *
   * @param memberNo member no
   * @return emitters
   */
  List<SseEmitter> findAllByMemberNo(Integer memberNo);

  /**
   * Delete all emitters by member no.
   *
   * @param memberNo member no
   */
  void deleteAll(Integer memberNo);

  /**
   * Delete emitter by member no & emitter.
   *
   * @param memberNo member no
   * @param emitter  emitter
   */
  void delete(Integer memberNo, SseEmitter emitter);
}
