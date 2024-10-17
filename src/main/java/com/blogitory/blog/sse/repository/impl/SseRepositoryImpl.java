package com.blogitory.blog.sse.repository.impl;

import com.blogitory.blog.sse.repository.SseRepository;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

/**
 * Implementation of sse repository.
 *
 * @author woonseok
 * @since 1.0
 **/
@Slf4j
@RequiredArgsConstructor
@Repository
public class SseRepositoryImpl implements SseRepository {
  private final Map<Integer, List<SseEmitter>> emitterMap = new ConcurrentHashMap<>();

  /**
   * {@inheritDoc}
   */
  @Override
  public SseEmitter save(Integer memberNo, SseEmitter emitter) {
    List<SseEmitter> emitters = emitterMap.get(memberNo);

    if (Objects.isNull(emitters)) {
      emitters = new CopyOnWriteArrayList<>();
    }

    emitters.add(emitter);

    emitterMap.put(memberNo, emitters);

    return emitter;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<SseEmitter> findAllByMemberNo(Integer memberNo) {
    return emitterMap.get(memberNo);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void deleteAll(Integer memberNo) {
    emitterMap.remove(memberNo);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void delete(Integer memberNo, SseEmitter emitter) {
    List<SseEmitter> emitters = emitterMap.get(memberNo);

    emitters.remove(emitter);
  }
}
