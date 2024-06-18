package com.blogitory.blog.rolemember.entity;

import com.blogitory.blog.member.entity.Member;
import com.blogitory.blog.role.entity.Role;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import java.io.Serializable;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * RoleMember entity.
 *
 * @author woonseok
 * @since 1.0
 **/
@Entity
@Table(name = "role_member")
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RoleMember {
  @EmbeddedId
  private Pk pk;

  @ManyToOne(fetch = FetchType.LAZY)
  @MapsId("roleNo")
  @JoinColumn(name = "role_no")
  private Role role;

  @ManyToOne(fetch = FetchType.LAZY)
  @MapsId("memberNo")
  @JoinColumn(name = "member_no")
  private Member member;

  /**
   * PK of RoleMember.
   */
  @Getter
  @EqualsAndHashCode
  @NoArgsConstructor(access = AccessLevel.PROTECTED)
  @AllArgsConstructor
  @Embeddable
  public static class Pk implements Serializable {
    @Column(name = "role_no")
    private Integer roleNo;
    @Column(name = "member_no")
    private Integer memberNo;
  }

  /**
   * Constructor without Pk.
   */
  public RoleMember(Role role, Member member) {
    this.pk = new Pk(role.getRoleNo(), member.getMemberNo());
    this.role = role;
    this.member = member;
  }
}
