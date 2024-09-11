package com.blogitory.blog.visitant.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.blogitory.blog.blog.entity.Blog;
import com.blogitory.blog.blog.entity.BlogDummy;
import com.blogitory.blog.blog.repository.BlogRepository;
import com.blogitory.blog.member.entity.Member;
import com.blogitory.blog.member.entity.MemberDummy;
import com.blogitory.blog.visitant.dto.VisitantInfoDto;
import com.blogitory.blog.visitant.entity.Visitant;
import com.blogitory.blog.visitant.entity.VisitantDummy;
import com.blogitory.blog.visitant.repository.VisitantRepository;
import com.blogitory.blog.visitant.service.impl.VisitantServiceImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * VisitantServiceTest.
 *
 * @author woonseok
 * @Date 2024-09-11
 * @since 1.0
 **/
class VisitantServiceTest {
  VisitantService visitantService;

  VisitantRepository visitantRepository;
  BlogRepository blogRepository;
  RedisTemplate<String, Object> redisTemplate;
  ObjectMapper objectMapper;

  @BeforeEach
  void setUp() {
    visitantRepository = mock(VisitantRepository.class);
    blogRepository = mock(BlogRepository.class);
    redisTemplate = mock(RedisTemplate.class);
    objectMapper = mock(ObjectMapper.class);

    visitantService = new VisitantServiceImpl(visitantRepository, blogRepository, redisTemplate, objectMapper);
  }

  @Test
  void viewBlogs() {
    HashOperations<String, Object, Object> hashOperations = mock(HashOperations.class);
    when(redisTemplate.opsForHash()).thenReturn(hashOperations);
    when(hashOperations.get(anyString(), anyString())).thenReturn(null);
    doNothing().when(hashOperations).put(anyString(), anyString(), anyString());

    visitantService.viewBlogs("blogUrl", 1, "127.0.0.1");

    verify(hashOperations, times(1)).put(any(), any(), any());
  }

  @Test
  void getVisitantCount() throws JsonProcessingException {
    HashOperations<String, Object, Object> hashOperations = mock(HashOperations.class);
    when(redisTemplate.opsForHash()).thenReturn(hashOperations);
    when(hashOperations.get(anyString(), anyString())).thenReturn(null);
    when(visitantRepository.getCountByBlogUrl(anyString())).thenReturn(null);
    when(visitantRepository.getCountByBlogUrlAndDate(anyString(), any())).thenReturn(null);
    when(objectMapper.readValue(anyString(), (Class<Object>) any())).thenThrow(IllegalArgumentException.class);

    Map<String, Integer> resultMap = visitantService.getVisitantCount("blogUrl");

    assertEquals(0, resultMap.get("total"));
    assertEquals(0, resultMap.get("today"));
  }

  @Test
  void persistence() throws JsonProcessingException {
    Member member = MemberDummy.dummy();
    Blog blog = BlogDummy.dummy(member);
    Visitant visitant = VisitantDummy.dummy(blog);
    HashOperations<String, Object, Object> hashOperations = mock(HashOperations.class);

    when(redisTemplate.opsForHash()).thenReturn(hashOperations);
    when(hashOperations.keys(anyString())).thenReturn(Set.of("key"));
    when(hashOperations.get(anyString(), any())).thenReturn("visitantString");
    when(objectMapper.readValue(anyString(), (Class<Object>) any()))
            .thenReturn(Set.of(new VisitantInfoDto(1, "127.0.0.1")));
    when(blogRepository.findBlogByUrlName(anyString()))
            .thenReturn(Optional.of(blog));
    when(visitantRepository.findByBlogUrlAndDate(anyString(), any()))
            .thenReturn(Optional.of(visitant));

    visitantService.persistence();

    verify(visitantRepository, times(1)).save(any());
  }

  @Test
  void saveAndDelete() throws JsonProcessingException {
    Member member = MemberDummy.dummy();
    Blog blog = BlogDummy.dummy(member);
    Visitant visitant = VisitantDummy.dummy(blog);
    HashOperations<String, Object, Object> hashOperations = mock(HashOperations.class);

    when(redisTemplate.opsForHash()).thenReturn(hashOperations);
    when(hashOperations.keys(anyString())).thenReturn(Set.of("key"));
    when(hashOperations.get(anyString(), any())).thenReturn("visitantString");
    when(objectMapper.readValue(anyString(), (Class<Object>) any()))
            .thenReturn(Set.of(new VisitantInfoDto(1, "127.0.0.1")));
    when(hashOperations.delete(anyString(), anyString())).thenReturn(1L);
    when(blogRepository.findBlogByUrlName(anyString()))
            .thenReturn(Optional.of(blog));
    when(visitantRepository.findByBlogUrlAndDate(anyString(), any()))
            .thenReturn(Optional.of(visitant));

    visitantService.saveAndDelete();

    verify(visitantRepository, times(1)).save(any());
  }
}