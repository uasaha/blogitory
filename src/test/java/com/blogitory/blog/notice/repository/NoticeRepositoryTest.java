package com.blogitory.blog.notice.repository;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.blogitory.blog.commons.config.JpaConfig;
import com.blogitory.blog.commons.config.QuerydslConfig;
import com.blogitory.blog.member.entity.Member;
import com.blogitory.blog.member.entity.MemberDummy;
import com.blogitory.blog.member.repository.MemberRepository;
import com.blogitory.blog.notice.entity.Notice;
import com.blogitory.blog.notice.entity.NoticeDummy;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

/**
 * Notice repository test.
 *
 * @author woonseok
 * @since 1.0
 */
@Import({JpaConfig.class, QuerydslConfig.class})
@DataJpaTest
class NoticeRepositoryTest {

  /**
   * The Member repository.
   */
  @Autowired
  MemberRepository memberRepository;

  /**
   * The Notice repository.
   */
  @Autowired
  NoticeRepository noticeRepository;

  /**
   * The Entity manager.
   */
  @Autowired
  EntityManager entityManager;

  /**
   * Teardown.
   */
  @AfterEach
  void teardown() {
    entityManager.createNativeQuery("ALTER TABLE `member` ALTER COLUMN `member_no` RESTART")
            .executeUpdate();
    entityManager.createNativeQuery("ALTER TABLE `notice` ALTER COLUMN `notice_no` RESTART")
            .executeUpdate();
  }

  /**
   * Notice save.
   */
  @Test
  @DisplayName("알림 저장")
  void noticeSave() {
    Member member = MemberDummy.dummy();
    member = memberRepository.save(member);

    Notice notice = NoticeDummy.dummy(member);
    Notice actual = noticeRepository.save(notice);

    assertAll(
            () -> assertEquals(notice.getNoticeNo(), actual.getNoticeNo()),
            () -> assertEquals(notice.getMember().getMemberNo(), actual.getMember().getMemberNo()),
            () -> assertEquals(notice.getUrl(), actual.getUrl()),
            () -> assertEquals(notice.getType(), actual.getType()),
            () -> assertEquals(notice.isRead(), actual.isRead())
    );
  }
}