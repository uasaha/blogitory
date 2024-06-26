package com.blogitory.blog.link.repository;

import com.blogitory.blog.link.entity.Link;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Link Repository for using JPA.
 *
 * @author woonseok
 * @since 1.0
 **/
public interface LinkRepository extends JpaRepository<Link, Long> {
}
