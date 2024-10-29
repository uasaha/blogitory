package com.blogitory.blog.notice.repository.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.blogitory.blog.commons.config.JpaConfig;
import com.blogitory.blog.commons.config.QuerydslConfig;
import com.blogitory.blog.member.entity.Member;
import com.blogitory.blog.member.entity.MemberDummy;
import com.blogitory.blog.member.repository.MemberRepository;
import com.blogitory.blog.notice.entity.Notice;
import com.blogitory.blog.notice.entity.NoticeDummy;
import com.blogitory.blog.notice.repository.NoticeRepository;
import jakarta.persistence.EntityManager;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * NoticeRepositoryTest.
 *
 * @author woonseok
 * @since 1.0
 **/
@Import({JpaConfig.class, QuerydslConfig.class})
@DataJpaTest
class NoticeRepositoryImplTest {

  @Autowired
  NoticeRepository noticeRepository;

  @Autowired
  MemberRepository memberRepository;

  @Autowired
  EntityManager entityManager;

  @AfterEach
  void tearDown() {
    entityManager.createNativeQuery("ALTER TABLE `member` ALTER COLUMN `member_no` RESTART")
            .executeUpdate();
    entityManager.createNativeQuery("ALTER TABLE `notice` ALTER COLUMN `notice_no` RESTART")
            .executeUpdate();
  }

  @Test
  void findLostNoticesByMemberNo() {
    Member member = MemberDummy.dummy();
    member = memberRepository.save(member);

    Notice notice = NoticeDummy.dummy(member);
    noticeRepository.save(notice);

    List<Notice> result = noticeRepository.findLostNoticesByMemberNo(member.getMemberNo(), 1L);

    assertTrue(result.isEmpty());
  }

  @Test
  void findAllNoticeByMemberNo() {
    Member member = MemberDummy.dummy();
    member = memberRepository.save(member);

    Notice notice = NoticeDummy.dummy(member);
    noticeRepository.save(notice);

    Pageable pageable = Pageable.ofSize(10);

    Page<Notice> result = noticeRepository.findAllNoticeByMemberNo(pageable, member.getMemberNo());

    assertEquals(1, result.getContent().size());
    assertEquals(1, result.getTotalElements());
  }

  @Test
  void hasNonReadNotice() {
    Member member = MemberDummy.dummy();
    member = memberRepository.save(member);

    Notice notice = NoticeDummy.dummy(member);
    noticeRepository.save(notice);

    boolean result = noticeRepository.hasNonReadNotice(member.getMemberNo());

    assertTrue(result);
  }
}