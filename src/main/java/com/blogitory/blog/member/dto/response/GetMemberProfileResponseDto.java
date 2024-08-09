package com.blogitory.blog.member.dto.response;

import com.blogitory.blog.blog.dto.response.GetBlogInProfileResponseDto;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Dto for Profile view.
 *
 * @author woonseok
 * @since 1.0
 **/
@Getter
@AllArgsConstructor
public class GetMemberProfileResponseDto {
  private String username;
  private String name;
  private String bio;
  private String profileThumb;
  private String introEmail;
  private List<GetMemberProfileLinkResponseDto> links;
  private List<GetBlogInProfileResponseDto> blogs;
  private Long followerCnt;
  private Long followeeCnt;
}
