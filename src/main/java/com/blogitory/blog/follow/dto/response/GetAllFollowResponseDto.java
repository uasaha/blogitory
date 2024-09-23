package com.blogitory.blog.follow.dto.response;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * GetAllFollowResponseDto.
 *
 * @author woonseok
 * @since 1.0
 **/
@Getter
@AllArgsConstructor
public class GetAllFollowResponseDto {
  private boolean isMine;
  private List<GetFollowResponseDto> followers;
  private List<GetFollowResponseDto> followings;
}
