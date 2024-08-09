package com.blogitory.blog.member.dto;

import com.blogitory.blog.member.dto.response.GetMemberPersistInfoDto;

/**
 * Member persis infos dto dummy.
 *
 * @author woonseok
 * @since 1.0
 */
public class MemberPersistInfoDtoDummy {
  /**
   * Dummy member persist info dto.
   *
   * @return the member persist info dto
   */
  public static GetMemberPersistInfoDto dummy() {
    return new GetMemberPersistInfoDto("username", "user", "thumb");
  }
}
