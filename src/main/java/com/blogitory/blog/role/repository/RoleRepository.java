package com.blogitory.blog.role.repository;

import com.blogitory.blog.role.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Jpa Repository for Role entity.
 *
 * @author woonseok
 * @since 1.0
 **/
public interface RoleRepository extends JpaRepository<Role, Long> {
}
