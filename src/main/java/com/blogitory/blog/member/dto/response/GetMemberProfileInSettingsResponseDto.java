package com.blogitory.blog.member.dto.response;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * User setting profile response dto.
 *
 * @author woonseok
 * @Date 2024-07-15
 * @since 1.0
 **/
@Getter
@AllArgsConstructor
public class GetMemberProfileInSettingsResponseDto {
  private String username;
  private String name;
  private String pfpUrl;
  private String email;
  private String bio;
  private String introEmail;
  private List<String> links;
}
