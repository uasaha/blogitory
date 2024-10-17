package com.blogitory.blog.sse.service.impl;

import com.blogitory.blog.commons.exception.NotFoundException;
import com.blogitory.blog.member.entity.Member;
import com.blogitory.blog.member.repository.MemberRepository;
import com.blogitory.blog.notice.dto.DefaultNoticeResponseDto;
import com.blogitory.blog.notice.entity.Notice;
import com.blogitory.blog.notice.repository.NoticeRepository;
import com.blogitory.blog.sse.exception.SseConnectionException;
import com.blogitory.blog.sse.repository.SseRepository;
import com.blogitory.blog.sse.service.SseService;
import java.io.IOException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

/**
 * Implementation of Sse service.
 *
 * @author woonseok
 * @since 1.0
 **/
@Slf4j
@RequiredArgsConstructor
@Service
public class SseServiceImpl implements SseService {

  private final MemberRepository memberRepository;
  private final NoticeRepository noticeRepository;

  private static final long DEFAULT_TIMEOUT = 600000L;
  private static final String NOTICE_CONNECTION_MSG = "Notification connected";

  private final SseRepository sseRepository;

  /**
   * {@inheritDoc}
   */
  @Transactional(readOnly = true, propagation = Propagation.REQUIRES_NEW)
  @Override
  public SseEmitter connect(Integer memberNo) {
    Member member = memberRepository.findById(memberNo)
            .orElseThrow(() -> new NotFoundException(Member.class));

    SseEmitter emitter = new SseEmitter(DEFAULT_TIMEOUT);

    sseRepository.save(member.getMemberNo(), emitter);

    emitter.onCompletion(() -> sseRepository.delete(memberNo, emitter));
    emitter.onTimeout(() -> sseRepository.delete(memberNo, emitter));

    try {
      emitter.send(SseEmitter.event()
              .name(NOTICE_CONNECTION_MSG));
    } catch (IOException e) {
      sseRepository.delete(memberNo, emitter);
      throw new SseConnectionException();
    }

    return emitter;
  }

  /**
   * {@inheritDoc}
   */
  @Transactional(readOnly = true)
  @Override
  public void checkAndSend(Integer memberNo, Long lastNoticeNo) {
    List<Notice> lostNotices = noticeRepository.findLostNoticesByMemberNo(memberNo, lastNoticeNo);

    lostNotices.forEach(notice ->
            send(notice.getNoticeNo(),
            notice.getMember().getMemberNo(),
            DefaultNoticeResponseDto.of(notice)));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void send(Long noticeNo, Integer targetMemberNo, Object body) {
    List<SseEmitter> emitters = sseRepository.findAllByMemberNo(targetMemberNo);

    emitters.forEach(emitter -> {
      try {
        emitter.send(SseEmitter.event()
                .id(String.valueOf(noticeNo))
                .data(body));
      } catch (Exception ex) {
        sseRepository.delete(targetMemberNo, emitter);
      }
    });
  }
}
