package com.blogitory.blog.tempposts.repository;

import com.blogitory.blog.tempposts.dto.GetTempPostsResponseDto;
import java.util.List;
import org.springframework.data.repository.NoRepositoryBean;

/**
 * Temp posts repository custom for using querydsl.
 *
 * @author woonseok
 * @since 1.0
 **/
@NoRepositoryBean
public interface TempPostsRepositoryCustom {
  /**
   * Get temp posts by member no.
   *
   * @param memberNo member no
   * @return Temp posts dto list
   */
  List<GetTempPostsResponseDto> getTempPostsByMemberNo(Integer memberNo);
}
