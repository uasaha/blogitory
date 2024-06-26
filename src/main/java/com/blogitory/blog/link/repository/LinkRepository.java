package com.blogitory.blog.link.repository;

import com.blogitory.blog.link.entity.Link;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 설명 작성 필!
 *
 * @author woonseok
 * @since 1.0
 **/
public interface LinkRepository extends JpaRepository<Link, Long> {
}
