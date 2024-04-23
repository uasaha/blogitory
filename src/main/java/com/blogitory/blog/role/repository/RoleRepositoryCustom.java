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
  List<String> findRolesByMemberNo(Integer memberNo);
}
