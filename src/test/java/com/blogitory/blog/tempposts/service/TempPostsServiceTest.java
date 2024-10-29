package com.blogitory.blog.tempposts.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.blogitory.blog.posts.dto.request.SaveTempPostsDto;
import com.blogitory.blog.tempposts.dto.GetTempPostsResponseDto;
import com.blogitory.blog.tempposts.repository.TempPostsRepository;
import com.blogitory.blog.tempposts.service.impl.TempPostsServiceImpl;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Temp posts service test.
 *
 * @author woonseok
 * @Date 2024-08-09
 * @since 1.0
 **/
class TempPostsServiceTest {

  TempPostsRepository tempPostsRepository;
  TempPostsService tempPostsService;

  @BeforeEach
  void setUp() {
    tempPostsRepository = mock(TempPostsRepository.class);
    tempPostsService = new TempPostsServiceImpl(tempPostsRepository);
  }

  @Test
  @DisplayName("임시 게시물 리스트 조회")
  void getTempPostsListByMemberNo() {
    UUID uuid = UUID.randomUUID();
    GetTempPostsResponseDto responseDto = new GetTempPostsResponseDto(uuid, "title", LocalDateTime.now());
    SaveTempPostsDto tpDto = new SaveTempPostsDto(
            1L,
            "url",
            "title",
            1L,
            "summary",
            "thumb",
            "details",
            List.of("tag"));

    when(tempPostsRepository.getTempPostsByMemberNo(anyInt())).thenReturn(List.of(responseDto));

    List<GetTempPostsResponseDto> results = tempPostsService.getTempPostsListByMemberNo(1);
    assertFalse(results.isEmpty());

    GetTempPostsResponseDto resultDto = results.getFirst();
    assertEquals(responseDto.getTempPostsId(), resultDto.getTempPostsId());
    assertEquals(tpDto.getTitle(), resultDto.getTitle());
  }
}