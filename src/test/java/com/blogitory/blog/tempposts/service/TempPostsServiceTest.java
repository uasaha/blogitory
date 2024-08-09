package com.blogitory.blog.tempposts.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.blogitory.blog.posts.dto.request.SaveTempPostsDto;
import com.blogitory.blog.posts.exception.PostsJsonConvertException;
import com.blogitory.blog.tempposts.dto.GetTempPostsResponseDto;
import com.blogitory.blog.tempposts.repository.TempPostsRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * Temp posts service test.
 *
 * @author woonseok
 * @Date 2024-08-09
 * @since 1.0
 **/
class TempPostsServiceTest {

  TempPostsRepository tempPostsRepository;
  RedisTemplate<String, Object> redisTemplate;
  ObjectMapper objectMapper;
  TempPostsService tempPostsService;

  @BeforeEach
  void setUp() {
    tempPostsRepository = mock(TempPostsRepository.class);
    redisTemplate = mock(RedisTemplate.class);
    objectMapper = mock(ObjectMapper.class);
    tempPostsService = new TempPostsServiceImpl(tempPostsRepository, redisTemplate, objectMapper);
  }

  @Test
  @DisplayName("임시 게시물 리스트 조회")
  void getTempPostsListByMemberNo() throws Exception {
    UUID uuid = UUID.randomUUID();
    GetTempPostsResponseDto responseDto = new GetTempPostsResponseDto(uuid, LocalDateTime.now());
    HashOperations<String, Object, Object> hashOperations = mock(HashOperations.class);
    String tp = "tp";
    SaveTempPostsDto postsDto = new SaveTempPostsDto(
            1L,
            null,
            "title",
            1L,
            "url",
            "summary",
            "thumb",
            "details",
            List.of("tag"));

    when(tempPostsRepository.getTempPostsByMemberNo(anyInt())).thenReturn(List.of(responseDto));
    when(redisTemplate.opsForHash()).thenReturn(hashOperations);
    when(hashOperations.get(anyString(), anyString())).thenReturn(tp);
    when(objectMapper.readValue(tp, SaveTempPostsDto.class)).thenReturn(postsDto);

    List<GetTempPostsResponseDto> results = tempPostsService.getTempPostsListByMemberNo(1);
    assertFalse(results.isEmpty());

    GetTempPostsResponseDto resultDto = results.getFirst();
    assertEquals(responseDto.getTempPostsId(), resultDto.getTempPostsId());
    assertEquals(postsDto.getTitle(), resultDto.getTempPostsTitle());
  }

  @Test
  @DisplayName("임시 게시물 리스트 조회 실패")
  void getTempPostsListByMemberNoFailed() throws Exception {
    UUID uuid = UUID.randomUUID();
    GetTempPostsResponseDto responseDto = new GetTempPostsResponseDto(uuid, LocalDateTime.now());
    HashOperations<String, Object, Object> hashOperations = mock(HashOperations.class);
    String tp = "tp";

    when(tempPostsRepository.getTempPostsByMemberNo(anyInt())).thenReturn(List.of(responseDto));
    when(redisTemplate.opsForHash()).thenReturn(hashOperations);
    when(hashOperations.get(anyString(), anyString())).thenReturn(tp);
    when(objectMapper.readValue(tp, SaveTempPostsDto.class)).thenThrow(JsonProcessingException.class);

    assertThrows(PostsJsonConvertException.class, () -> tempPostsService.getTempPostsListByMemberNo(1));
  }
}