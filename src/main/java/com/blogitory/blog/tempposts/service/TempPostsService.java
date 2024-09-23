package com.blogitory.blog.tempposts.service;

import com.blogitory.blog.tempposts.dto.GetTempPostsResponseDto;
import java.util.List;

/**
 * Temp posts service.
 *
 * @author woonseok
 * @since 1.0
 **/
public interface TempPostsService {

  /**
   * Get temp posts list by member no.
   *
   * @param memberNo member no
   * @return temp posts list
   */
  List<GetTempPostsResponseDto> getTempPostsListByMemberNo(Integer memberNo);
}
