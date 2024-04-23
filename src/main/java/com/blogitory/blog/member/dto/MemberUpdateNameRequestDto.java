package com.blogitory.blog.member.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Dto for update name request.
 *
 * @author woonseok
 * @since 1.0
 **/
@Getter
@NoArgsConstructor
public class MemberUpdateNameRequestDto {
  String name;
}
