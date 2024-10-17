package com.blogitory.blog.notice.repository;

import com.blogitory.blog.notice.entity.Notice;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.NoRepositoryBean;

/**
 * Notice custom repository.
 *
 * @author woonseok
 * @since 1.0
 **/
@NoRepositoryBean
public interface NoticeRepositoryCustom {

  /**
   * Find lost notice by member no.
   *
   * @param memberNo     member no
   * @param lastNoticeNo last sent notice no
   * @return notices
   */
  List<Notice> findLostNoticesByMemberNo(Integer memberNo, Long lastNoticeNo);

  /**
   * Find all notice by member no.
   *
   * @param pageable pageable
   * @param memberNo member no
   * @return notices
   */
  Page<Notice> findAllNoticeByMemberNo(Pageable pageable, Integer memberNo);

  /**
   * Has not read notice by member no.
   *
   * @param memberNo member no
   * @return is read
   */
  boolean hasNonReadNotice(Integer memberNo);
}
