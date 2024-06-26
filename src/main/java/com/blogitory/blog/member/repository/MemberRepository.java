package com.blogitory.blog.member.repository;

import com.blogitory.blog.member.entity.Member;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 * Jpa Repository for Member entity.
 *
 * @author woonseok
 * @since 1.0
 **/
public interface MemberRepository extends JpaRepository<Member, Integer>, MemberRepositoryCustom {
  /**
   * Checking for duplicated email.
   *
   * @param email new email
   * @return is duplicated
   */
  boolean existsMemberByEmail(String email);

  /**
   * Find Member By Email.
   *
   * @param email email
   * @return Member entity
   */
  Optional<Member> findByEmail(String email);

  /**
   * Checking for duplicated username.
   *
   * @param username username
   * @return is duplicated
   */
  boolean existsMemberByUsername(String username);

  @Query("select m.profileThumb from Member m where m.memberNo = ?1")
  String findProfileThumbByMemberNo(Integer memberNo);

  Optional<Member> findByUsername(String username);
}
