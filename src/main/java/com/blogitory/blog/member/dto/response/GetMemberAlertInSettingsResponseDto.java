package com.blogitory.blog.member.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Get alert setting response dto.
 *
 * @author woonseok
 * @since 1.0
 **/
@Getter
@AllArgsConstructor
public class GetMemberAlertInSettingsResponseDto {
  boolean followAlert;
  boolean commentAlert;
  boolean heartAlert;
  boolean newPostsAlert;
}
