package com.blogitory.blog.rolemember.entity;

import com.blogitory.blog.member.entity.Member;
import com.blogitory.blog.member.entity.MemberDummy;
import com.blogitory.blog.role.entity.Role;
import com.blogitory.blog.role.entity.RoleDummy;

/**
 * Test data of RoleMember.
 *
 * @author woonseok
 * @since 1.0
 **/
public class RoleMemberDummy {
  public static RoleMember dummy(Role role, Member member) {
    return new RoleMember(
            new RoleMember.Pk(role.getRoleNo(), member.getMemberNo()), role, member);
  }

  public static RoleMember dummy() {
    return dummy(RoleDummy.dummy(), MemberDummy.dummy());
  }
}
