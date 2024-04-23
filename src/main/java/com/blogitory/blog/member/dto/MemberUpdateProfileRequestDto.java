package com.blogitory.blog.member.dto;

import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Dto for update profile content.
 *
 * @author woonseok
 * @since 1.0
 **/
@Getter
@NoArgsConstructor
public class MemberUpdateProfileRequestDto {
  @Size(max = 50)
  private String content;
}
