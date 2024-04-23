package com.blogitory.blog.member.dto;

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
public class MemberPersistInfoDto {
  private String name;
  private String thumb;
}
