package com.blogitory.blog.member.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Get alert setting response dto.
 *
 * @author woonseok
 * @Date 2024-07-15
 * @since 1.0
 **/
@Getter
@AllArgsConstructor
public class MemberSettingsAlertResponseDto {
  boolean followAlert;
  boolean commentAlert;
  boolean heartAlert;
  boolean newPostsAlert;
}
