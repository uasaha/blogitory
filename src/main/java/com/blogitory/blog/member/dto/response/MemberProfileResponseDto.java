package com.blogitory.blog.member.dto.response;

import com.blogitory.blog.blog.dto.response.BlogProfileResponseDto;
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
public class MemberProfileResponseDto {
  private String username;
  private String name;
  private String bio;
  private String profileThumb;
  private String introEmail;
  private List<MemberProfileLinkResponseDto> links;
  private List<BlogProfileResponseDto> blogs;
  private Long followerCnt;
  private Long followeeCnt;
}
