package com.blogitory.blog.notice.repository;

import com.blogitory.blog.notice.entity.Notice;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Notice Repository.
 *
 * @author woonseok
 * @since 1.0
 **/
public interface NoticeRepository extends JpaRepository<Notice, Long> {
}
