package com.blogitory.blog.rolemember.repository;

import com.blogitory.blog.rolemember.entity.RoleMember;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Jpa Repository for RoleMember entity.
 *
 * @author woonseok
 * @since 1.0
 **/
public interface RoleMemberRepository extends JpaRepository<RoleMember, RoleMember.Pk> {
}
