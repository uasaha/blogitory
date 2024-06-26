package com.blogitory.blog.member.repository;

import com.blogitory.blog.member.dto.MemberPersistInfoDto;
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
  Optional<MemberPersistInfoDto> getPersistInfo(Integer memberNo);
}
