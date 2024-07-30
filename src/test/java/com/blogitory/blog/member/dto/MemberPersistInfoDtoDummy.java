package com.blogitory.blog.member.dto;

import com.blogitory.blog.member.dto.response.MemberPersistInfoDto;

/**
 * 설명 작성 필!
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
  public static MemberPersistInfoDto dummy() {
    return new MemberPersistInfoDto("username", "user", "thumb");
  }
}
