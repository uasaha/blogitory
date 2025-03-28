package com.blogitory.blog.member.repository;

import com.blogitory.blog.member.dto.response.GetMemberPersistInfoDto;
import com.blogitory.blog.member.entity.Member;
import java.util.Optional;
import org.springframework.data.repository.NoRepositoryBean;

/**
 * Member Repository Custom for using Querydsl.
 *
 * @author woonseok
 * @since 1.0
 **/
@NoRepositoryBean
public interface MemberRepositoryCustom {

  /**
   * Get user info for page's header.
   *
   * @param memberNo user number
   * @return user info
   */
  Optional<GetMemberPersistInfoDto> getPersistInfo(Integer memberNo);

  /**
   * Get user by oauth id.
   *
   * @param provider oauth provider
   * @param id       oauth id
   * @return member
   */
  Optional<Member> findByOauthId(String provider, String id);


  /**
   * Find Member by email or username.
   *
   * @param id email or username
   * @return Member entity
   */
  Optional<Member> findByEmailOrUsername(String id);
}
