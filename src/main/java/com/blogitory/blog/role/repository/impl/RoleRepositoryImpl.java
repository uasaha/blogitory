package com.blogitory.blog.role.repository.impl;


import com.blogitory.blog.member.entity.QMember;
import com.blogitory.blog.role.entity.QRole;
import com.blogitory.blog.role.entity.Role;
import com.blogitory.blog.role.repository.RoleRepositoryCustom;
import com.blogitory.blog.rolemember.entity.QRoleMember;
import java.util.List;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

/**
 * Implementation of Role repository for using querydsl.
 *
 * @author woonseok
 * @since 1.0
 **/
public class RoleRepositoryImpl extends QuerydslRepositorySupport implements RoleRepositoryCustom {
  public RoleRepositoryImpl() {
    super(Role.class);
  }

  @Override
  public List<String> findRolesByMemberNo(Integer memberNo) {
    QRoleMember roleMember = QRoleMember.roleMember;
    QMember member = QMember.member;
    QRole role = QRole.role;

    return from(roleMember)
            .select(role.roleName)
            .innerJoin(member).on(roleMember.member.memberNo.eq(member.memberNo))
            .innerJoin(role).on(roleMember.role.roleNo.eq(role.roleNo))
            .where(roleMember.member.memberNo.eq(memberNo))
            .fetch();
  }
}
