package com.blogitory.blog.role.service.impl;

import com.blogitory.blog.role.repository.RoleRepository;
import com.blogitory.blog.role.service.RoleService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementation of RoleService.
 *
 * @author woonseok
 * @since 1.0
 **/
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {
  private final RoleRepository roleRepository;

  @Override
  public List<String> getRolesByMemberNo(Integer memberNo) {
    return roleRepository.findRolesByMemberNo(memberNo);
  }
}
