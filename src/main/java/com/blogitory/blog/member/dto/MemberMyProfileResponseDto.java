package com.blogitory.blog.member.dto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Dto for getting my profile response.
 *
 * @author woonseok
 * @since 1.0
 **/
@AllArgsConstructor
@Getter
public class MemberMyProfileResponseDto {
  private String email;
  private String name;
  private String profileThumb;
  private String introEmail;
  private String github;
  private String twitter;
  private String facebook;
  private String homepage;
  private LocalDateTime createdAt;
}
