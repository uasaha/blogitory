package com.blogitory.blog.member.repository;

import com.blogitory.blog.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Jpa Repository for Member entity.
 *
 * @author woonseok
 * @since 1.0
 **/
public interface MemberRepository extends JpaRepository<Member, Long> {
  boolean existsMemberByEmail(String email);

  boolean existsMemberByName(String name);
}
