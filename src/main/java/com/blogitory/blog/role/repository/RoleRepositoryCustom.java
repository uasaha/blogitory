package com.blogitory.blog.role.repository;

import java.util.List;
import org.springframework.data.repository.NoRepositoryBean;

/**
 * Custom repository for using querydsl.
 *
 * @author woonseok
 * @since 1.0
 **/
@NoRepositoryBean
public interface RoleRepositoryCustom {
  /**
   * Find roles by memberNo.
   *
   * @param memberNo memberNo
   * @return roles
   */
  List<String> findRolesByMemberNo(Integer memberNo);
}
