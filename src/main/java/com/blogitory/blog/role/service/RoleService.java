package com.blogitory.blog.role.service;

import java.util.List;

/**
 * Role Service.
 *
 * @author woonseok
 * @since 1.0
 **/
public interface RoleService {
  List<String> getRolesByMemberNo(Integer memberNo);
}
