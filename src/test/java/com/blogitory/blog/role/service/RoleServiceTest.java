package com.blogitory.blog.role.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.blogitory.blog.role.repository.RoleRepository;
import com.blogitory.blog.role.service.impl.RoleServiceImpl;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Role service test.
 *
 * @author woonseok
 * @since 1.0
 **/
class RoleServiceTest {
  RoleRepository roleRepository;
  RoleService roleService;

  @BeforeEach
  void setUp() {
    roleRepository = mock(RoleRepository.class);
    roleService = new RoleServiceImpl(roleRepository);
  }

  @Test
  void getRolesByMemberNo() {
    Integer memberNo = 1;
    List<String> result = List.of("ROLE_DUMMY");

    when(roleRepository.findRolesByMemberNo(any())).thenReturn(result);

    List<String> actual = roleService.getRolesByMemberNo(memberNo);

    assertEquals(result.size(), actual.size());
    assertEquals(result.get(0), actual.get(0));
  }
}