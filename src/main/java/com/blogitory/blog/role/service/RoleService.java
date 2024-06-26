package com.blogitory.blog.role.service;

import java.util.List;

/**
 * Role Service.
 *
 * @author woonseok
 * @since 1.0
 **/
public interface RoleService {

  /**
   * Get roles by memberNo.
   *
   * @param memberNo memberNo
   * @return roles
   */
  List<String> getRolesByMemberNo(Integer memberNo);
}
