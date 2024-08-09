package com.blogitory.blog.member.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Dto for User status on.
 *
 * @author woonseok
 * @since 1.0
 **/
@Getter
@AllArgsConstructor
public class GetMemberPersistInfoDto {
  private String username;
  private String name;
  private String thumb;
}
