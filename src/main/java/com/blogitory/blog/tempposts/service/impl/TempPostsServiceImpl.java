package com.blogitory.blog.tempposts.service.impl;

import com.blogitory.blog.tempposts.dto.GetTempPostsResponseDto;
import com.blogitory.blog.tempposts.repository.TempPostsRepository;
import com.blogitory.blog.tempposts.service.TempPostsService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Temp posts service implementation.
 *
 * @author woonseok
 * @since 1.0
 **/
@Service
@Transactional
@RequiredArgsConstructor
public class TempPostsServiceImpl implements TempPostsService {
  private final TempPostsRepository tempPostsRepository;

  /**
   * {@inheritDoc}
   */
  @Override
  @Transactional(readOnly = true)
  public List<GetTempPostsResponseDto> getTempPostsListByMemberNo(Integer memberNo) {
    return tempPostsRepository
            .getTempPostsByMemberNo(memberNo);
  }
}
